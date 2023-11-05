package com.example.demo.image;


import com.example.demo.board.BoardDslRepository;
import com.example.demo.board.BoardRepository;
import com.example.demo.comment.CommentDslRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ImageService {
    @Autowired
    ImageDataRepository imageDataRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired
    BoardDslRepository boardJpaRepository;
    @Autowired
    CommentDslRepository commentDslRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    S3FileDeleter fileDeleter;
    // 이거 가져올 때 항상 Board정보가 있어야되는데..
    // edit에서 가져오는 건지 아니면, 이전 정보 수정 정보에서 가져오는 건지
    // 항상 id 정보를 같이 보내고, 이 정보를 조회하게 해야될듯 ㅇㅇ..
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(ImageStore imageStore) {
        // boardJpaRepository.saveImage(imageStore);
        imageRepository.save(imageStore);
    }

    public void delete(ImageStore imageStore) {
        imageRepository.delete(imageStore);
    }

    // 이미지 다 불러와서 한번에 지워아지 ㅇㅇ..
    @Transactional
    public void deleteExpiredBoardAndImage() throws ParseException {
        ImageStore imageStore = new ImageStore();
        List<ImageBoardDto> images = imageDataRepository.findExpiredImage();
        System.out.println("images.size() = " + images.size());
        Set<Long> deleteBoardImages = new HashSet<>();
        images.forEach(imageBoardDto -> {
            // System.out.println("imageBoardDto.getBoardId() = " + imageBoardDto.getFilePath());

            if(imageBoardDto.getFilePath() != null) {
                File file = new File(imageBoardDto.getFilePath());
                file.delete();
            }
            deleteBoardImages.add(imageBoardDto.getBoardId());

        });

        imageDataRepository.delete(deleteBoardImages);
        commentDslRepository.deleteParentComment(deleteBoardImages);
        // board -> image 조회가 맞음
        System.out.println("deleteBoardImages.size() = " + deleteBoardImages.size());
        boardRepository.deleteAllByIdInBatch(deleteBoardImages);
    }
    /*
    e = org.springframework.dao.DataIntegrityViolationException: could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement
    이것 때문에 동시성 이슈걸려서 배치 프로세스에 트랜잭션을 걸었기 떄문에, 사용자 트랜잭션 수정-> 배치 트랜잭션
    삭제가 일어나도 사용자 트랜잭션이 먼저 작동하는걸 보장해서 사용함(다른 트랜잭션이라도 하나의 데이터라 의미가 있다는 말임)
     */
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delteExpiredBoardAndImage2() throws ParseException {
        List<ImageBoardDto>  expiredImageBoardDtos = imageDataRepository.findExpiredImage();
        deleteData(expiredImageBoardDtos);
    }
    public void deleteBoard(Long boardId) {
        List<ImageBoardDto> imageBoardDtos = imageDataRepository.findImage(boardId);
        deleteData(imageBoardDtos);
    }

    private void deleteData(List<ImageBoardDto>imageBoardDtos) {
        System.out.println("images.size() = " + imageBoardDtos.size());
        Set<Long> deleteBoardImages = new HashSet<>();

        List<String> files = new ArrayList<>();
        // System.out.println("imageBoardDto.getBoardId() = " + imageBoardDto.getFilePath());
        for (ImageBoardDto imageBoardDto:imageBoardDtos){
            System.out.println("imageBoardDto.getFilePath() = " + imageBoardDto.getFilePath());
            //null 비교 안하면 데이터가 Null도 추가되서 S3 병렬 삭제 이슈생겨서 이렇게 해야됨
            if(imageBoardDto.getFilePath()!=null) {
                files.add(imageBoardDto.getFilePath());
            }
            System.out.println("imageBoardDto.getBoardId() = " + imageBoardDto.getBoardId());

            deleteBoardImages.add(imageBoardDto.getBoardId());
        }

        commentDslRepository.delete(deleteBoardImages);
        imageDataRepository.deleteParentComment(deleteBoardImages);

        boardRepository.deleteAllByIdInBatch(deleteBoardImages);
        fileDeleter.deleteFiles(files);

        // board -> image 조회가 맞음
        System.out.println("deleteBoardImages.size() = " + deleteBoardImages.size());
    }
}


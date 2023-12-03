package com.example.demo.board.image;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.example.demo.board.Board;
import com.example.demo.board.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@Slf4j
public class ImageController {

    @Autowired
    private S3Uploader s3Uploader;
    @Autowired
    private S3Reader s3Reader;
    @Autowired
    private BoardService boardService;
    @Autowired
    private ImageService imageService;

    @GetMapping("/images/{key}")
    @ApiOperation("이미지 조회")
    public ResponseEntity<byte[]> getImage(@PathVariable String key) {
        System.out.println(key);
        byte[] data = s3Reader.read(key);
        System.out.println(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(data.length);
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }


    // board 삭제  / 가져오기 동시 발생
    // 배치위에 @Transactional(propagation = Propagation.REQUIRES_NEW) 썼으니
    // 사용자 업로드 -> 배치 삭제시 순서대로 동작하는 거 보장
    // 단 배치 삭제 -> 사용자 업로드인 경우 둘 중 하나 동작하나 누가 동작하는지는 모름
    @Value("${domain.url}")
    private String fileUrl;
    @PostMapping("/image/upload/{id}")
    @ApiOperation("이미지 업로드")
    @ResponseBody
    public String file(HttpServletRequest request, HttpServletResponse response,
                       MultipartHttpServletRequest multipartRequest, @PathVariable Long id) throws Exception {
        MultipartFile multipartFile = multipartRequest.getFile("upload");
        if (isImage(multipartFile)) {
            String fileName = s3Uploader.upload(multipartFile);
            Board board = null;
            board = boardService.findAllByImageId(id);
            if(board == null) {
                board = new Board();
                board.setId(id);
                boardService.saveBoard(board);
            }
            ImageStore imageStore = new ImageStore(fileName, LocalDateTime.now(), board);
            imageService.save(imageStore);
            System.out.println("imageStore = " + imageStore);
            String fileApiUrl = fileUrl+"images/" + fileName;

            System.out.println("fileApiUrl = " + fileApiUrl);

            return "{\"filename\" : \"" + fileName + "\", \"uploaded\" : 1, \"url\":\"" + fileApiUrl + "\"}";
        }
        throw new Exception("파일이 없습니다");
    }
    public boolean isImage(MultipartFile multipartFile) {
        if(multipartFile.getOriginalFilename().endsWith(".jpg")||multipartFile.getOriginalFilename().endsWith(".png")||
                multipartFile.getOriginalFilename().endsWith(".jpeg")||multipartFile.getOriginalFilename().endsWith(".gif")) {
            return true;
        }
        return false;
    }

    @Autowired
    private S3FileDeleter s3FileDeleter;

    @DeleteMapping("/delete/{objectKey}")
    @ApiOperation("이미지 삭제하기")
    public ResponseEntity<String> deleteFile(@PathVariable String objectKey) {
        System.out.println("objectKey = " + objectKey);
        try {
            // AWS S3 버킷 이름
            s3FileDeleter.deleteFile(objectKey);
            return ResponseEntity.ok("File deleted successfully!");
        } catch (AmazonServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file from S3: " + e.getMessage());
        } catch (SdkClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to connect to S3: " + e.getMessage());
        }
    }

}



package com.example.demo.comment;


import com.example.demo.boradAndUser.UserAndBoardDto;
import com.example.demo.util.cookie.CookieManager;
import com.example.demo.entityjoin.*;
import com.example.demo.mongo.ChildCommentService;
import com.example.demo.mongo.ParentCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class CommentService {
    // @Autowired
    //CommentDslRepository commentDslRepository;
    // @Autowired ParentCommentRepository parentCommentRepository;
    //@Autowired
    //JoinDslRepository joinDslRepository;
    @Autowired
    ParentCommentService parentCommentService;
    @Autowired
    ChildCommentService childCommentService;
    @Autowired CommentMapper commentMapper;
    @Autowired
    CookieManager cookieManager;
    // @Autowired ChildCommentRepository childCommentRepository;

    public void saveParentComment(ParentCommentSaveViewDto commentViewDto, HttpServletRequest request) throws Exception {
        String uuidCookie = cookieManager.getUUidCookie(request);
        ParentComment parentComment = commentMapper.saveMemberAndComment(commentViewDto,uuidCookie);
        parentCommentService.insertParentComment(parentComment);

    }
    public void saveChildComment(ChildCommentSaveViewDto childCommentSaveViewDto, HttpServletRequest request) throws Exception {
        String uuidCookie = cookieManager.getUUidCookie(request);
        ChildComment childComment = commentMapper.saveMemberAndComment(childCommentSaveViewDto,uuidCookie);
        parentCommentService.addChildComment(childComment);
    //     childCommentRepository.save(childComment);
    }

    public void saveOnlyParentComment(String content, UserAndBoardDto userAndBoardDto) throws Exception {
        ParentComment parentComment = commentMapper.saveOnlyCommendDto(content,userAndBoardDto);
        System.out.println("saveOnly comment = " + parentComment);
        parentCommentService.insertParentComment(parentComment);
        // parentCommentRepository.save(comment);
    }
    public void saveOnlyChildComment(String content, UserAndBoardAndParentCommentDto userAndBoardAndParentCommentDto) throws Exception {
        ChildComment childComment = commentMapper.saveOnlyCommendDto(content,userAndBoardAndParentCommentDto);
        System.out.println("saveOnly comment = " + childComment);
        parentCommentService.addChildComment(childComment);
        childCommentService.insertChildComment(childComment);

    }
    @Transactional
    public void saveParentComments(List<ParentCommentSaveViewDto> parentCommentSaveViewDtos) throws Exception {
        List<ParentComment> parentComments = commentMapper.saveCommendDtos(parentCommentSaveViewDtos);
        // parentCommentRepository.saveAll(comments);
        parentCommentService.insertAllParentComment(parentComments);
    }

    public List<ParentComment> readTenParentComment(CommentReadViewDto commentViewDto) throws Exception {
        // List<Long> parentCommentIndex = commentDslRepository.findByIdsSubQueryParentComment(commentViewDto.getStartId(), commentViewDto.getBoardId());
        // System.out.println("parentCommentIndex.size() = " + parentCommentIndex.size());
        // return joinDslRepository.findByIdsSubQueryParentComment(parentCommentIndex);
        return parentCommentService.getParentCommentsByBoardId(commentViewDto.getBoardId());
    }

}


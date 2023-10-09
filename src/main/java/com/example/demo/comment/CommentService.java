package com.example.demo.comment;


import com.example.demo.cookie.CookieManager;
import com.example.demo.entityjoin.*;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDslRepository commentDslRepository;
    @Autowired ParentCommentRepository parentCommentRepository;
    @Autowired
    JoinDslRepository joinDslRepository;
    @Autowired CommentMapper commentMapper;
    @Autowired
    CookieManager cookieManager;
    // 이거 데이터 수정해야됨
    public void saveParentComment(CommentSaveViewDto commentViewDto, HttpServletRequest request) throws Exception {
        String uuidCookie = cookieManager.getUUidCookie(request);
        ParentComment comment = commentMapper.saveMemberAndComment(commentViewDto,uuidCookie);
        parentCommentRepository.save(comment);
    }

    public void saveOnlyParentComment(String content, UserAndBoardDto userAndBoardDto) throws Exception {
        ParentComment comment = commentMapper.saveOnlyCommendDto(content,userAndBoardDto);
        System.out.println("saveOnly comment = " + comment);
        parentCommentRepository.save(comment);
    }
    @Transactional
    public void saveParentComments(List<CommentSaveViewDto> commentSaveViewDtos) throws Exception {
        List<ParentComment> comments = commentMapper.saveCommendDtos(commentSaveViewDtos);
        parentCommentRepository.saveAll(comments);
    }

    public List<CommentReadDataDto> readTenParentComment(CommentReadViewDto commentViewDto) throws Exception {
        List<Long> byIdsSubQuery = commentDslRepository.findByIdsSubQuery(commentViewDto.getStartId(), commentViewDto.getBoardId());
        return joinDslRepository.findByIdsSubQuery(byIdsSubQuery);
    }

}


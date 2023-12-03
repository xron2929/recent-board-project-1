package com.example.demo.feedback;

import com.example.demo.util.request.RequestIpApi;
import com.example.demo.security.jwt.JwtManager;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LikeController {
    @Autowired
    FeedBackCountService feedBackCountApi;
    @Autowired
    JwtManager jwtManager;
    @Autowired
    RequestIpApi requestIpApi;
    @ApiOperation("게시판 좋아요 처리")
    @PutMapping("/board/like")
    @ResponseBody
    public Long updateNoneUserLikeCount(@RequestBody JoinStatusIdDto joinStatusIdDto,
                                        HttpServletRequest request) {
        String ip =requestIpApi.getClientIpAddr(request);
        System.out.println("LikeController.updateNoneUserLikeCount()");
        System.out.println("LikeController.updateNoneUserLikeCount() client Ip"+ip);
        return feedBackCountApi.isExistenceLike(JoinStatus.BOARD, joinStatusIdDto.getJoinStatusId(),
                ip);
    }
    @ApiOperation("게시판 싫어요 처리")
    @PutMapping("/board/dislike")
    @ResponseBody
    public Long updateNoneUserDisLikeCount(@RequestBody JoinStatusIdDto joinStatusIdDto,
                                           HttpServletRequest request) {
        return feedBackCountApi.isExistenceDisLike(JoinStatus.BOARD,joinStatusIdDto.getJoinStatusId(),
                requestIpApi.getClientIpAddr(request));
    }

}

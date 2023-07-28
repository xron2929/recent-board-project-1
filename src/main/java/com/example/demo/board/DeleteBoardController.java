package com.example.demo.board;

import com.example.demo.role.RoleStatus;
import com.example.demo.image.ImageService;
import com.example.demo.entityjoin.DeleteBoardDto;
import com.example.demo.entityjoin.NoneUserBoardSaveDataDto;
import com.example.demo.security.authentication.AuthenticationManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.UserIdAndPasswordDto;
import com.example.demo.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

@Controller
public class DeleteBoardController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    BoardService boardService;
    @Autowired
    UserService userService;
    @Autowired
    ImageService imageService;
    @Autowired
    JwtManager jwtManager;

    @GetMapping("/board/none-user/delete/{boardId}")
    @ApiOperation("none-user의 경우 delete을 할 때 비밀번호를 직접 입력해야하는 뷰 반환")
    public String getDeleteView(@PathVariable Long boardId) {
        return "board/delete/none-user-delete-view";
    }
    // 얘는 폼 이 아니라 fetch로 전송하고 Location.href 이동
    @DeleteMapping("/board/none-user")
    @ApiOperation("작성자가 none_user이고 none_user가 board를 삭제하려는 경우 데이터 처리 삭제 처리")
    @ResponseBody
    public String deleteNoneUserBoard(@RequestBody DeleteBoardDto deleteBoardDto) throws ParseException {
        System.out.println("deleteBoardDto.getBoardId() = " + deleteBoardDto.getBoardId());
        System.out.println("deleteBoardDto.getPassword() = " + deleteBoardDto.getPassword());
        NoneUserBoardSaveDataDto noneUserPasswordBoard = boardService.findNoneUserPasswordBoard(deleteBoardDto.getBoardId());
        if(noneUserPasswordBoard.getPassword().equals(deleteBoardDto.getPassword())) {
            System.out.println("noneUserPasswordBoard success");
            imageService.deleteBoard(deleteBoardDto.getBoardId());
            return "ok";
        }
        return "password가 틀렸습니다";
    }

    @DeleteMapping("/board/admin/{boardId}")
    @ApiOperation("board를 삭제하려는 사람이 admin인 경우 삭제")
    @ResponseBody
    public String deleteAdminBoard(@PathVariable Long boardId, HttpServletRequest request, HttpServletResponse response) throws ParseException, JsonProcessingException {
        String isExistence = authenticationManager.checkAuthenticationManager(request, response);
        if(isExistence == "null") {
            return null;
        }
        String accessToken = jwtManager.getAccessToken(request);
        // return accessToken; 연결을 userId로 바꾸려고 시도중 마지막한거
        String authorityName = jwtManager.getAuthorityName(accessToken);
        System.out.println("deleteAdminBoard - authorityName.equals(RoleStatus.ROLE_ADMIN.name()) = " + authorityName.equals(RoleStatus.ROLE_ADMIN.name()));
        if(authorityName.equals(RoleStatus.ROLE_ADMIN.name())) {
            imageService.deleteBoard(boardId);
        }

        return "ok";
    }

    @DeleteMapping("/board/user/{boardId}")
    @ApiOperation("작성자가 siteUser나 oauthUser같은 일반 유저이고, 삭제 하는 사람이 admin인 경우 삭제")
    @ResponseBody
    public String deleteUserBoard(@PathVariable Long boardId, HttpServletRequest request) throws Exception {
        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(jwtManager.getAccessToken(request));
        UserIdAndPasswordDto userIdAndPasswordDto = boardService.findUserIdAndPassword(boardId);
        if(isEqualUserIdAndPassword(userRequestDto,userIdAndPasswordDto)) {
            imageService.deleteBoard(boardId);
            return "ok";
        }
        return "uncorrect user";
    }

    boolean isEqualUserIdAndPassword(UserRequestDto userRequestDto,UserIdAndPasswordDto userIdAndPasswordDto) {
        if(!userIdAndPasswordDto.getUserId().equals(userRequestDto.getUserId())) {
            return false;
        }
        if(!userIdAndPasswordDto.getPassword().equals(userIdAndPasswordDto.getPassword())) {
            return false;
        }
        return true;
    }

}


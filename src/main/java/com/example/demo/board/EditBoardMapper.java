package com.example.demo.board;


import com.example.demo.user.api.UserAuthorityCheckApi;
import com.example.demo.user.authority.Authority;
import com.example.demo.util.cookie.CookieManager;
import com.example.demo.boradAndUser.NoneUserBoardSaveDataDto;
import com.example.demo.boradAndUser.UserBoardSaveDataDto;
import com.example.demo.util.request.RequestIpApi;
import com.example.demo.role.RoleStatus;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.UserService;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.noneuser.NoneMember;
import com.example.demo.user.userAuthority.UserAuthority;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class EditBoardMapper {
    @Autowired
    public BoardService boardService;
    @Autowired
    public UserService userService;
    @Autowired
    CookieManager cookieManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtManager jwtManager;
    @Autowired
    UserAuthorityCheckApi adminApi;
    @Autowired
    RequestIpApi requestIpApi;
    public void setNoneUserBoardUpdateDataDto(NoneUserBoardSaveDataDto noneuserBoardSaveDataDto) {

        DefaultMember user = DefaultMember.builder()
                .id(noneuserBoardSaveDataDto.getId())
                .userId(noneuserBoardSaveDataDto.getUserId())
                .nickname(noneuserBoardSaveDataDto.getNickname())
                .password(noneuserBoardSaveDataDto.getPassword()).build();
        Board board = new Board(noneuserBoardSaveDataDto.getId(), noneuserBoardSaveDataDto.getTitle(),
                noneuserBoardSaveDataDto.getContent(),noneuserBoardSaveDataDto.isSecret());
        boardService.updateBoard(board);
        userService.updateUser(user);
    }

    public void setUserBoardUpdateDataDto(UserBoardSaveViewDto userBoardSaveViewDto, UserBoardSaveDataDto userBoardSaveDataDto, String refreshToken, HttpServletResponse response) throws JsonProcessingException {

        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(refreshToken);
        DefaultMember userByUserId = userService.findUserByUserId(userRequestDto.getUserId());
        boolean testData = adminApi.checkAuthority(userByUserId, userBoardSaveDataDto);
        System.out.println(" EditBoardMapper - setUserBoardUpdateDataDto() testData = "+testData);
        if(testData) {
            // Member user = new Member(boardSaveDataDto.getId(),boardSaveDataDto.getAuthor(), boardSaveDataDto.getPassword());
            Board board = new Board(userBoardSaveViewDto.getBoardId(),
                    userBoardSaveViewDto.getTitle(),
                    userBoardSaveViewDto.getContent(),
                    userBoardSaveViewDto.isSecret());
            boardService.updateBoard(board);
        }

    }
    public void insertUserBoard(BoardEditUserDto boardEditUserDto, UserRequestDto userRequestDto) {
        // Member user = new Member(boardSaveDataDto.getId(),boardSaveDataDto.getAuthor(), boardSaveDataDto.getPassword());
        System.out.println("UserBoardSaveDataDto - userBoardSaveDataDto.getUsername() = " + userRequestDto.getUserId());
        DefaultMember user = userService.findUserByUserId(userRequestDto.getUserId());
        System.out.println("user.getId() = " + user.getId());
        Board board = new Board(boardEditUserDto.getId(),boardEditUserDto.getTitle(), boardEditUserDto.getContent(),
                user,boardEditUserDto.isSecret());
        Board board1 = boardService.saveBoard(board);
        System.out.println("board1.getTitle() = " + board1.getTitle());
        System.out.println("board.getMember() = " + board.getMember());
        System.out.println("board1.getContent() = " + board1.getContent());
    }
    public void insertNoneUserBoard( HttpServletRequest request,NoneUserBoardSaveDataDto userBoardSaveDataDto) {
        System.out.println("UserBoardSaveDataDto - userBoardSaveDataDto.getNickname() = " + userBoardSaveDataDto.getNickname());
        System.out.println(" EditBoardMapper.insertNoneUserBoard() uuidCookie = " + cookieManager.getUUidCookie(request));
        System.out.println("boardRequestDto.getUsername() = " + userBoardSaveDataDto.getContent());
        List<UserAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(new UserAuthority(new Authority(RoleStatus.ROLE_ANONYMOUS.name())));
        NoneMember member = NoneMember.builder()
                .nickname(userBoardSaveDataDto.getNickname())
                .userId(cookieManager.getUUidCookie(request))
                .password(userBoardSaveDataDto.getPassword())
                .ip(requestIpApi.getClientIpAddr(request))
                .userAuthorities(userAuthorities)
                .build();
        userService.userAndUserAuthoritySave(member);
        Board board = new Board(userBoardSaveDataDto.getId(),userBoardSaveDataDto.getTitle(),
                userBoardSaveDataDto.getContent(),member,userBoardSaveDataDto.isSecret());
        boardService.saveBoard(board);
    }


}


package com.example.demo.board;


import com.example.demo.api.MemberApi;
import com.example.demo.authority.Authority;
import com.example.demo.cookie.CookieManager;
import com.example.demo.entityjoin.NoneUserBoardSaveDataDto;
import com.example.demo.entityjoin.UserBoardSaveDataDto;
import com.example.demo.role.RoleStatus;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.UserService;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.noneuser.NoneMember;
import com.example.demo.userAuthority.UserAuthority;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
    JwtManager jwtManager;
    @Autowired
    MemberApi memberApi;
    public void setNoneuserBoardUpdateDataDto(NoneUserBoardSaveDataDto noneuserBoardSaveDataDto) {
        DefaultMember user = DefaultMember.builder().userId(noneuserBoardSaveDataDto.getUsername())
                .password(noneuserBoardSaveDataDto.getPassword()).build();
        Board board = new Board(noneuserBoardSaveDataDto.getId(), noneuserBoardSaveDataDto.getTitle(),
                noneuserBoardSaveDataDto.getContent(),noneuserBoardSaveDataDto.isSecret());
        boardService.updateBoard(board);
        userService.updateUser(user);
    }

    public void setUserBoardUpdateDataDto(UserBoardSaveViewDto userBoardSaveViewDto, UserBoardSaveDataDto userBoardSaveDataDto,String refreshToken) throws JsonProcessingException {
        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(refreshToken);
        if(!memberApi.checkUserIdAndPassword(refreshToken, userBoardSaveDataDto.getUserId())) {
            System.out.println("EditBoardMapper - setUserBoardUpdateDataDto 검증에 실패하였습니다 = ");
            return;
        }
        // Member user = new Member(boardSaveDataDto.getId(),boardSaveDataDto.getAuthor(), boardSaveDataDto.getPassword());
        DefaultMember user = DefaultMember.builder().id(userBoardSaveViewDto.getBoardId())
                .userId(userRequestDto.getUserId())
                .password(userRequestDto.getPassword()).build();
        Board board = new Board(userBoardSaveViewDto.getBoardId(), userBoardSaveViewDto.getTitle(),
                userBoardSaveViewDto.getContent(),userBoardSaveViewDto.isSecret());
        boardService.updateBoard(board);
        userService.updateUser(user);
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
    public void insertNoneUserBoard(HttpServletRequest request,NoneUserBoardSaveDataDto userBoardSaveDataDto) {
        System.out.println("UserBoardSaveDataDto - userBoardSaveDataDto.getUsername() = " + userBoardSaveDataDto.getUsername());
        System.out.println("boardRequestDto.getUsername() = " + userBoardSaveDataDto.getContent());
        List<UserAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(new UserAuthority(new Authority(RoleStatus.ROLE_ANONYMOUS.name())));
        NoneMember member = NoneMember.builder()
                .userId(userBoardSaveDataDto.getUsername())
                .password(userBoardSaveDataDto.getPassword())
                .ip(request.getRemoteAddr())
                .uuid(cookieManager.getUUidCookie(request))
                .userAuthorities(userAuthorities)
                .build();
        userService.userAndUserAuthoritySave(member);
        Board board = new Board(userBoardSaveDataDto.getId(),userBoardSaveDataDto.getTitle(),
                userBoardSaveDataDto.getContent(),member,userBoardSaveDataDto.isSecret());
        boardService.saveBoard(board);
    }


}


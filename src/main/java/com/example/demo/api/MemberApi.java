package com.example.demo.api;

import com.example.demo.board.UserBoardSaveViewDto;
import com.example.demo.entityjoin.UserBoardSaveDataDto;
import com.example.demo.role.RoleStatus;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.userAuthority.UserAuthority;

import javax.management.relation.Role;
import java.util.List;

public class MemberApi {

    public JwtManager jwtManager;
    public boolean checkUserId(String userId,String checkUserId) {
        if(userId.equals(checkUserId)) {
            System.out.println("CheckStatus.SAME_USER.getCheckMessage() = " + CheckStatus.SAME_USER.getCheckMessage());
            return CheckStatus.SAME_USER.getCheckMessage();
        }
        System.out.println("CheckStatus.UN_SAME_USER.getCheckMessage(); = " + CheckStatus.UN_SAME_USER.getCheckMessage());
        return CheckStatus.UN_SAME_USER.getCheckMessage();
    }
    public boolean checkAuthority(DefaultMember boardChangeTrialUser, UserBoardSaveDataDto boardAuthorityUser) {
        if(!checkUserId(boardChangeTrialUser.getUserId(), boardAuthorityUser.getUserId())) {
            List<UserAuthority> boardChangeTrialUserAuthorities = boardChangeTrialUser.getUserAuthorities();
            for(UserAuthority boardChangeTrialUserAuthority: boardChangeTrialUserAuthorities) {
                System.out.println("boardChangeTrialUserAuthority.getAuthority().getAuthorityName() = " + boardChangeTrialUserAuthority.getAuthority().getAuthorityName());
                boolean b = (boardChangeTrialUserAuthority.getAuthority().getAuthorityName() == RoleStatus.ROLE_ADMIN.name());
                if(boardChangeTrialUserAuthority.getAuthority().getAuthorityName().equals(RoleStatus.ROLE_ADMIN.name())) {

                    System.out.println("MemberApi - checkAuthority() - CheckStatus.UN_SAME_USER_BUT_ADMIN_TRY.getCheckMessage(); = " + CheckStatus.UN_SAME_USER_BUT_ADMIN_TRY.getCheckMessage());
                    return CheckStatus.UN_SAME_USER_BUT_ADMIN_TRY.getCheckMessage();
                }
            }
            System.out.println("CheckStatus.UN_SAME_USER_AND_UN_ADMIN_TRY.getCheckMessage(); = " + CheckStatus.UN_SAME_USER_AND_UN_ADMIN_TRY.getCheckMessage());
            return CheckStatus.UN_SAME_USER_AND_UN_ADMIN_TRY.getCheckMessage();
        }
        return CheckStatus.SAME_USER.getCheckMessage();
    }
}

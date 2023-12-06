package com.example.demo.util;


import com.example.demo.board.UserAuthorityAndUserIdDto;
import com.example.demo.role.RoleStatus;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.TokenStatus;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.IsAllowedAuthorityStatus;
import com.example.demo.user.UserIdAndIsAllowedAuthorityStatus;
import com.example.demo.user.userAuthority.UserAuthority;
import com.example.demo.util.cookie.CookieManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UserManager {
    JwtManager jwtManager;
    CookieManager cookieManager;
    PasswordEncoder passwordEncoder;
    public UserManager(JwtManager jwtManager,CookieManager cookieManager,PasswordEncoder passwordEncoder) {
        this.jwtManager = jwtManager;
        this.cookieManager = cookieManager;
        this.passwordEncoder = passwordEncoder;
    }

    // O
    public UserIdAndValidationDtoAndAccessToken getUserAccountStatus(HttpServletRequest request, UserIdAndValidationDtoAndAccessToken userIdAndValidationDtoAndAccessToken, String dbPassword) throws JsonProcessingException {
        if(userIdAndValidationDtoAndAccessToken.getValidationStatus()== ValidationStatus.NONE_USER_ACCOUNT) {
            return userIdAndValidationDtoAndAccessToken;
        }
        String accessToken = jwtManager.getAccessToken(request);
        return getAndValidateUser(accessToken,dbPassword);
        // password가 상황마다 달라서 다른 방법 써야됨
    }
    // O
    public UserIdAndIsAllowedAuthorityStatus checkUserIdAndValidation(UserAuthorityAndUserIdDto userAuthorityAndUserIdDto, HttpServletRequest request) throws JsonProcessingException {
        String accessToken = jwtManager.getAccessToken(request);
        TokenStatus tokenValidation = jwtManager.validation(accessToken);
        String boardReadUserId;
        if (tokenValidation == TokenStatus.NONE || tokenValidation == TokenStatus.TOKEN_ERROR) {
            boardReadUserId = cookieManager.getUUidCookie(request);
            if(userAuthorityAndUserIdDto.getUserAuthorityName().equals(RoleStatus.ROLE_ANONYMOUS.name())&&
                    userAuthorityAndUserIdDto.getUserId().equals(boardReadUserId)) {
                return new UserIdAndIsAllowedAuthorityStatus(boardReadUserId, IsAllowedAuthorityStatus.SAME_USER_ACCOUNT);
            }
            return new UserIdAndIsAllowedAuthorityStatus(boardReadUserId, IsAllowedAuthorityStatus.UN_SAME_USER_ACCOUNT);
        }
        String boardReadAuthority = jwtManager.getAuthorityName(accessToken);
        UserRequestDto boardReadUserRequestDto = jwtManager.getUserRequestDto(accessToken);
        if(boardReadAuthority.equals(RoleStatus.ROLE_ADMIN.name())) {
            return new UserIdAndIsAllowedAuthorityStatus(boardReadUserRequestDto.getUserId(), IsAllowedAuthorityStatus.HIGHER_READER_ACCOUNT);
        }
        if(boardReadUserRequestDto.getUserId().equals(userAuthorityAndUserIdDto.getUserId()) &&
                boardReadAuthority.equals(userAuthorityAndUserIdDto.getUserAuthorityName())) {
            return new UserIdAndIsAllowedAuthorityStatus(boardReadUserRequestDto.getUserId(), IsAllowedAuthorityStatus.SAME_USER_ACCOUNT);
        }
        return new UserIdAndIsAllowedAuthorityStatus(boardReadUserRequestDto.getUserId(), IsAllowedAuthorityStatus.UN_SAME_USER_ACCOUNT);
    }

    public UserIdAndValidationDtoAndAccessToken getUserIdAndValidationDtoAndAccessToken(HttpServletRequest request) throws JsonProcessingException {
        String accessToken = jwtManager.getAccessToken(request);
        TokenStatus tokenValidation = jwtManager.validation(accessToken);
        if(tokenValidation == TokenStatus.NONE ||
                tokenValidation == TokenStatus.TOKEN_ERROR) {
            return UserIdAndValidationDtoAndAccessToken.createUserIdAndValidationDto
                    (ValidationStatus.NONE_USER_ACCOUNT,
                            cookieManager.getUUidCookie(request));
        }
        return UserIdAndValidationDtoAndAccessToken.createUserIdAndValidationDtoAndAccessToken
                (ValidationStatus.UN_CHECK_USER_ACCOUNT,
                        jwtManager.getUserRequestDto(accessToken).getUserId(),
                        accessToken);
    }
    public UserRequestDto getUserRequestDto(HttpServletRequest request) throws JsonProcessingException {
        String accessToken = jwtManager.getAccessToken(request);
        TokenStatus tokenValidation = jwtManager.validation(accessToken);
        if(tokenValidation == TokenStatus.NONE ||
                tokenValidation == TokenStatus.TOKEN_ERROR) {
            return null;
        }
        return jwtManager.getUserRequestDto(accessToken);
    }
    public UserIdAndValidationDtoAndAccessToken getAndValidateUser(String accessToken, String dbPassword) throws JsonProcessingException {
        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(accessToken);
        String userId = userRequestDto.getUserId();
        String password = userRequestDto.getPassword();
        System.out.println("AlarmController - userId = " + userId);
        System.out.println("AlarmController - password = " + password);
        UserIdAndValidationDtoAndAccessToken getOauthUserIdAndValidationDtoAndAccessToken = validateOauthPassword(userId,userRequestDto);
        if(getOauthUserIdAndValidationDtoAndAccessToken.getValidationStatus() != ValidationStatus.UN_CHECK_USER_ACCOUNT) {
            return getOauthUserIdAndValidationDtoAndAccessToken;
        }
        return validateSiteOrAdminPassword(userId,password,dbPassword);

    }
    public UserIdAndValidationDtoAndAccessToken validateOauthPassword(String userId, UserRequestDto userRequestDto) {
        if(passwordEncoder.matches("겟인데요",userRequestDto.getPassword())) {
            System.out.println("isExistUserIdAndPassword - success");
            // offset 처리를 추가로 해야됨
            return UserIdAndValidationDtoAndAccessToken.createUserIdAndValidationDto(ValidationStatus.USER_ACCOUNT,userId);
        }
        return UserIdAndValidationDtoAndAccessToken.createUserIdAndValidationDto(ValidationStatus.UN_CHECK_USER_ACCOUNT,userId);
    }
    // O
    public UserIdAndValidationDtoAndAccessToken validateSiteOrAdminPassword(String userId, String password, String dbPassword) {
        if(passwordEncoder.matches(password,dbPassword)) {
            return UserIdAndValidationDtoAndAccessToken.createUserIdAndValidationDto(ValidationStatus.USER_ACCOUNT,userId);
        }
        return UserIdAndValidationDtoAndAccessToken.createUserIdAndValidationDto(ValidationStatus.ERROR_ACCOUNT,userId);
    }
    // O
    public ResponseAuthenticationDto getResponseAuthenticationDto(ResponseAuthenticationDto responseAuthenticationDto,UserRequestDto userRequestDto) {
        List<UserAuthority> userAuthorities = userRequestDto.getUserAuthorities();
        for (UserAuthority userAuthority:userAuthorities) {
            if(userAuthority.getAuthority().getAuthorityName().matches(RoleStatus.ROLE_ADMIN.name())) {
                responseAuthenticationDto.setRole("어드민");
                return responseAuthenticationDto;
            }
            if(userAuthority.getAuthority().getAuthorityName().matches(RoleStatus.ROLE_SITE_USER.name())) {
                responseAuthenticationDto.setRole("일반 회원가입 유저");
                return responseAuthenticationDto;
            }
            if(userAuthority.getAuthority().getAuthorityName().matches(RoleStatus.ROLE_OAUTH_USER.name())) {
                responseAuthenticationDto.setRole("OAUTH 유저");
                return responseAuthenticationDto;
            }
            if(userAuthority.getAuthority().getAuthorityName().matches(RoleStatus.ROLE_ANONYMOUS.name())) {
                responseAuthenticationDto.setRole("비회원");
                return responseAuthenticationDto;
            }
        }
        responseAuthenticationDto.setRole("권한 에러");
        return responseAuthenticationDto;
    }


}


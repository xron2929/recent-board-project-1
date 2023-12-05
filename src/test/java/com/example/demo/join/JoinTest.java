package com.example.demo.join;

import com.example.demo.security.details.PrincipalDetails;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.Trans;
import com.example.demo.security.jwt.TransManager;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.security.provider.*;
import com.example.demo.security.service.PrincipalOauth2UserService;
import com.example.demo.user.UserService;
import com.example.demo.user.authority.Authority;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.gender.Gender;
import com.example.demo.user.oauthuser.OauthMember;
import com.example.demo.user.siteuser.SiteMember;
import com.example.demo.user.userAuthority.UserAuthority;
import com.example.demo.util.cookie.CookieManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.querydsl.core.NonUniqueResultException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

@SpringBootTest
@ExtendWith({SpringExtension.class})
@ActiveProfiles(profiles = {"test"})
public class JoinTest {

    @Autowired
    TransManager transManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    CookieManager cookieManager;
    @Autowired
    JwtManager jwtManager;

    @Autowired
    MockMvc mockMvc;

    private SiteMember setSiteUser(Trans trans,UserRequestDto userRequestDto,UserAuthority userAuthority) {
        Gender gender = new Gender(trans);
        SiteMember user = UserRequestDto.from(userRequestDto,trans);
        user.setGender(gender);
        ArrayList<UserAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(userAuthority);
        user.setUserAuthorities(userAuthorities);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    @DisplayName("일반 사이트 회원과 어드민 회원은 userId를 가질 수 없다.")
    @Test
    void canNotInsertSameSiteUserId() throws RuntimeException{
        //given
        UserRequestDto userRequestDto1 = UserRequestDto.builder()
                .userId("ab")
                .age(19l)
                .email("a@gmail.com")
                .nickname("a")
                .trans("남성")
                .phoneNumber("010-8434-0311")
                .password("1234").build();
        UserRequestDto userRequestDto2 = UserRequestDto.builder()
                .userId("ab")
                .age(20l)
                .email("ab@gmail.com")
                .nickname("ab")
                .trans("남성")
                .phoneNumber("010-8434-0319")
                .password("1234").build();

        Authority authority = new Authority("ROLE_SITE_USER");
        UserAuthority userAuthority = new UserAuthority(authority);

        Trans trans1 = transManager.getTrans(userRequestDto1.getTrans());
        Trans trans2 = transManager.getTrans(userRequestDto2.getTrans());

        SiteMember siteUser1 = setSiteUser(trans1,userRequestDto1,userAuthority);
        SiteMember siteUser2 = setSiteUser(trans2,userRequestDto2,userAuthority);

        SiteMember findUserByUserId1 = userService.findBySiteMemberId(userRequestDto1.getUserId());

        if(findUserByUserId1!=null) {
            userService.deleteSiteUser(findUserByUserId1);
        }
        SiteMember findUserByUserId2 = userService.findBySiteMemberId(userRequestDto2.getUserId());
        if(findUserByUserId2!=null) {
            userService.deleteSiteUser(findUserByUserId2);
        }


        // when
        SiteMember findSiteUser1 = userService.saveSiteUser(siteUser1);
        Exception duliactionSiteUserException = assertThrows(RuntimeException.class, () ->
                userService.saveSiteUser(siteUser2));
        // then
        duliactionSiteUserException.getMessage().equals("사이트 또는 어드민 회원은 동일한 userId를 가진 테이블을 삽입할 수 없습니다.");
    }

    private MockCookie getCookie(String name, String value, int second) {
        MockCookie cookie = new MockCookie(name,value);
        cookie.setMaxAge(second);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
    @DisplayName("oauth 회원은 userId를 가질 수 없다.")
    @Test
    void canNotInsertSameOauthUserId() throws Exception {
        long age= 32l;
        String trans="남자";
        String nickname="abc0";
        MockCookie cookie1 = getCookie("age",String.valueOf(age),10*60);
        MockCookie cookie2 = getCookie("trans",trans,10*60);
        MockCookie cookie3 = getCookie("nickname",nickname,10*60);
        mockMvc.perform(get("/oauth2/authorization/google").cookie(cookie1,cookie2,cookie3));
    }


}

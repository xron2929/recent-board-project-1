package com.example.demo.security.service;


import com.example.demo.user.authority.Authority;
import com.example.demo.user.UserService;
import com.example.demo.user.gender.Gender;
import com.example.demo.security.jwt.*;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.defaultuser.UserRepository;
import com.example.demo.user.oauthuser.OauthMember;
import com.example.demo.user.siteuser.SiteMember;
import com.example.demo.user.userAuthority.UserAuthorityRepository;
import com.example.demo.util.cookie.CookieManager;
import com.example.demo.security.details.PrincipalDetails;
import com.example.demo.security.provider.*;
import com.example.demo.user.userAuthority.UserAuthority;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Component
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    // 구글로 받은 userRequest 데이터에 대한 후처리 되는 함수
    // 내일은 꼭 Jwt + Oauth 연동
    @Autowired
    HttpServletResponse response;
    @Autowired
    HttpServletRequest request;
    @Autowired
    RefreshTokenManager refreshTokenManager;
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAuthorityRepository userAuthorityRepository;
    @Autowired
    UserService userService;
    @Autowired
    JwtManager jwtManager;

    @Value("${redirect-url}")
    private String redirectUrl;

    @Value("${jwt.secretkey}")
    String secretKey;
    Key key;

    @PostConstruct
    private void makeKey() {
        secretKey = "c2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK";
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("request = " + request);
        // System.out.println("userRequest = " + request.getRemoteAddr());
        System.out.println("ip = " + response); // 서버에서 실행 시 외부ip 보여지는 거 맞음

        System.out.println("re = " + redirectUrl);
        log.info("userRequest.getClientRegistration() = {}" + userRequest.getClientRegistration());
        log.info("userRequest.getAccessToken() = " + userRequest.getAccessToken().getTokenValue());
        log.info("userRequest.getClientRegistration().getId() = {}" , userRequest.getClientRegistration().getRegistrationId());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Oauth2UserInfo oauth2UserInfo = null;
        String url = null;
        CookieManager cookieManager = new CookieManager();
        TransManager transManager = new TransManager();
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println(" 구글 로그인 요청 ");
            System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
            oauth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            // response.setHeader("url",redirectUrl+"google");
            // response.sendRedirect("http://localhost:8080/exceptiontest");
            // redirect 전략은 하나라서 안됨
            // 장기적으로 봐서 url은 한번만 값 가지고 있으면 되는데, 사용자가 오래 사용할 수록 서버 메모리 비용도 몇 배로
            // 계속 빠져나감
            url = redirectUrl + "google";
            // session.setMaxInactiveInterval(60);
            cookieManager.makeSecurityCookie("url",url,60*60,response);
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println(" 페이스북 로그인 요청 ");
            oAuth2User = super.loadUser(userRequest);
            oauth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
            url = redirectUrl + "facebook";
            cookieManager.makeSecurityCookie("url",url,60*60,response);
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println(" 네이버 로그인 요청 ");
            oauth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
            System.out.println("oauth2UserInfo = " + oauth2UserInfo);
            url = redirectUrl + "naver";
            cookieManager.makeSecurityCookie("url",url,60*60,response);
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oauth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            url = redirectUrl + "kakao";
            cookieManager.makeSecurityCookie("url",url,60*60,response);
        }
        else {
            System.out.println(" 구글과 페이스북,네이버 카카오 로그인만 지원합니다 " );
        }

        // 회원가입을 강제로 진행해볼 예정
        String provider = oauth2UserInfo.getProvider();
        String providerId = oauth2UserInfo.getProviderId();
        String username = provider  + "_"  + providerId;
        String password = bCryptPasswordEncoder.encode("겟인데요");
        String email = oauth2UserInfo.getEmail();
        List<UserAuthority> userAuthorities = new ArrayList<>();
        Authority authority = new Authority("ROLE_OAUTH_USER");
        UserAuthority userAuthority = new UserAuthority(authority);
        userAuthorities.add(userAuthority);
        SiteMember userEntity = userService.findSiteUserByUserId(username);
// userService에서 호출해서 트랜잭션관리 따로 해야될듯 ㅇㅇ..
        // 나중에 도메인이면 따로관리하긴해야하는데 타임리프로 클린 코드 해봐야될듯
        // /String phoneNumber = (String) request.getSession().getAttribute("phoneNumber");
        Long age = null;
        String stringAge = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("age"))
                .map(cookie -> cookie.getValue()).findFirst().orElse(null);
        if(stringAge != null) {
            age = Long.valueOf(stringAge);
        }

        String phoneNumber = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("phoneNumber"))
                .map(cookie -> cookie.getValue()).findFirst().orElse( null);
        String trans = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("trans"))
                .map(cookie -> cookie.getValue()).findFirst().orElse( null);
        String nickname = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("nickname"))
                .map(cookie -> cookie.getValue()).findFirst().orElse(null);

        cookieManager.makeZeroSecondCookie("trans",response);
        cookieManager.makeZeroSecondCookie("nickname",response);
        // 한글 텍스트를 쿠키 상태로 가지고 있으면 인터셉터에 걸려서 끊기는 경우가 많아서 그냥 바로 삭제
        // 다시 생성
        // 만약 있다면 그대로 반환
        String accessToken = new String();
        String refreshToken = new String();
        log.info("loadUser");
        accessToken = jwtManager.getAccessToken(request);
        log.info("accessToken = {}",accessToken);
        UserRequestDto joinUserRequestDto = UserRequestDto
                .builder()
                .email(email)
                .age(age)
                .trans(trans)
                .userId(username)
                .password(password)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .userAuthorities(userAuthorities)
                .build();
        joinUserRequestDto.getUserAuthorities().stream().map(userAuthority1 -> userAuthority1.getAuthority())
                .map(authority1 -> authority1.getAuthorityName())
                .forEach(authorityName -> log.info("authorityName = {}",authorityName));


        System.out.println("real accessToken = " + accessToken);

        if(userEntity != null) {
            try {

                UserRequestDto loginUserRequestDto = UserRequestDto
                        .builder()
                        .email(userEntity.getEmail())
                        .age(userEntity.getAge())
                        .trans(userEntity.getGender().getTrans().name())
                        .userId(userEntity.getUserId())
                        .password(password)
                        .nickname(userEntity.getNickname())
                        .phoneNumber(userEntity.getPhoneNumber())
                        .userAuthorities(userEntity.getUserAuthorities())
                        .build();

                accessToken = jwtManager.setAccessToken(request,response,loginUserRequestDto);
                refreshToken = jwtManager.setRefreshToken(request,response,loginUserRequestDto);
                Authentication authentication = getAuthentication(loginUserRequestDto.getUserId(), accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("SecurityContextHolder.getContext().getAuthentication() = " + SecurityContextHolder.getContext().getAuthentication());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // 로그인
            System.out.println(" login ");
            return new PrincipalDetails(userEntity,oAuth2User.getAttributes(),accessToken);
        }
        System.out.println("url = " + url);
        // additionalInformationRedisTemplate.setTimeOutRedisTemplate("url",url);
        if(userEntity == null && phoneNumber== null) {
            System.out.println("1");
            throw new OAuth2AuthenticationException("첫 계정 가입");
        }
        if(userEntity == null && phoneNumber.isBlank()) {
            System.out.println("2");
            throw new OAuth2AuthenticationException("첫 계정 가입");
        }
        if(userEntity == null && age == null) {
            System.out.println("3");
            throw new OAuth2AuthenticationException("첫 계정 가입");
        }
        if(userEntity == null && age.describeConstable().isEmpty()) {
            System.out.println("4");
            throw new OAuth2AuthenticationException("첫 계정 가입");
        }
        // 토큰 만료
        if(age==null && phoneNumber == null) {
            System.out.println("5");
            throw new OAuth2AuthenticationException("첫 계정 가입");
        }
        System.out.println("age = " + age);
        System.out.println("phoneNumber = " + phoneNumber);
        System.out.println(" 첫 회원 가입 ");
        onlyJoinUsingToken(joinUserRequestDto);
        // userEntity = new Member(username,password,email,provider,providerId,age,phoneNumber,userAuthorities);
        userEntity = OauthMember
                .builder()
                .userId(username)
                .gender(new Gender(transManager.getTrans(trans)))
                .nickname(nickname)
                .password(password)
                .email(email)
                .userId(provider+"_"+providerId)
                .age(age)
                .phoneNumber(phoneNumber)
                .userAuthorities(userAuthorities)
                .build();

        userEntity.getUserAuthorities().forEach(userAuthority1 -> userAuthority1.getAuthority().getAuthorityName());
        userEntity.setModifiedAt(LocalDateTime.now());
        // userRepository.save(userEntity);
        cookieManager.makeZeroSecondCookie("nickname",response);
        cookieManager.makeZeroSecondCookie("phoneNumber",response);
        cookieManager.makeZeroSecondCookie("age",response);
        userService.userAndUserAuthoritySave(userEntity);
        try {
            Authentication authentication = getAuthentication(userRequestDto.getUserId(),accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication1 = " + authentication1);
            System.out.println("authentication1.getPrincipal() = " + authentication1.getPrincipal());
            System.out.println("authentication1.getPrincipal().getClass() = " + authentication1.getPrincipal().getClass());
            System.out.println("authentication1.getDetails() = " + authentication1.getDetails());
            System.out.println("authentication1.getCredentials = " + authentication1.getCredentials());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // cascade 한 객체라서 User 그대로 가져오면 안됨
        return new PrincipalDetails(userEntity,oAuth2User.getAttributes(),accessToken,url);
        // Oauth로 들어개ㅣ면 PrincipalDetails에서 생성자 2개 // 반대면 AccountDetailsService에서 한 개
    }

    public Authentication getAuthentication(String userId,String accessToken) throws JsonProcessingException {
        DefaultMember defaultMember = userService.findUserByUserId(userId);
        System.out.println("defaultMember = " + defaultMember);
        // 얘를 db에서 가져와야됨
        // User user = UserRequestDto.from(userRequestDto);
        PrincipalDetails principalDetails = new PrincipalDetails(defaultMember);
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("accessToken", accessToken);
        principalDetails.setAttributes(userAttributes);
        log.info("principalDetails22 = '{}'",principalDetails);
        return new UsernamePasswordAuthenticationToken(principalDetails,accessToken,principalDetails.getAuthorities());
    }
    public void onlyJoinUsingToken(UserRequestDto joinUserRequestDto) {
        try {

            String refreshToken = jwtManager.getRefreshToken(request);
            log.info("refreshToken = {}",refreshToken);
            if(refreshToken == null || refreshTokenManager.validaition(refreshToken,key) == false) {
                log.info("success2");
                System.out.println("joinUserRequestDto = " + joinUserRequestDto);
                log.info("PrincipalOauth2UserService - refreshToken = {}",refreshToken);
                String accessToken = jwtManager.setAccessToken(request,response,joinUserRequestDto);
                refreshToken = jwtManager.setRefreshToken(request,response,joinUserRequestDto);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

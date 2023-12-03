package com.example.demo.user.join;


import com.example.demo.user.authority.Authority;
import com.example.demo.user.email.EmailTemplate;
import com.example.demo.user.gender.Gender;
import com.example.demo.user.gender.GenderService;
import com.example.demo.security.jwt.Trans;
import com.example.demo.security.jwt.TransApi;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.ResponseOauthDto;
import com.example.demo.user.UserService;
import com.example.demo.user.siteuser.SiteMember;
import com.example.demo.user.userAuthority.UserAuthority;
import com.example.demo.user.userAuthority.UserAuthorityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
// User 태그 적용
public class JoinController {
    @Autowired
    UserService userService;
    @Autowired
    UserAuthorityService userAuthorityService;
    @Autowired
    EmailTemplate emailTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    GenderService genderService;
    @Autowired
    EmailValidator emailValidator;
    private static final String SPECIAL_SYMBOLS[] = {
            "-","^","+","-","=","*","/"
    };
    @GetMapping("/first/oauth/join")
    @ApiOperation("oauth 가입시 age랑 phoneNumber 먼저 입력하도록 하는 뷰 반환")
    public String addAge(HttpServletRequest request, HttpServletResponse response) {
        return "join/oauth-join";
    }

    @GetMapping("/oauth/check")
    @ApiOperation("oauth 계정 추가할 때 nickname이나 phoneNumber가 이미 존재하는지 유효성 검사")
    @ResponseBody
    public String isAllowedJoin(@RequestParam(value = "phoneNumber", required = true) String phoneNumber,
                                 @RequestParam(value = "nickname", required = true) String nickname) throws isExistenceUserDataException {
        String duplicatePhoneNumberAndNicknameResult  = userService.findByPhoneNumberOrNickname(phoneNumber, nickname);
        return emailValidator.isExistencePhoneNumberOrNickname(duplicatePhoneNumberAndNicknameResult);
    }
    @PostMapping("/add/session")
    @ApiOperation("oauth 추가할 때 age랑 phoneNumber도 같이 추가")
    public String addSession(@ModelAttribute @Validated ResponseOauthDto responseOauthDto, HttpServletResponse response,
                             HttpServletRequest request, BindingResult bindingResult) throws JsonProcessingException {
        if(bindingResult.hasErrors()) {
            return "redirect:first/oauth/join";
        }

        long age = responseOauthDto.getAge();
        String phoneNumber = responseOauthDto.getPhoneNumber();
        String nickname = responseOauthDto.getNickname();
        String trans = responseOauthDto.getTrans();
        response.addCookie(getCookie("age",String.valueOf(age),10* 60));
        response.addCookie(getCookie("phoneNumber",phoneNumber,10* 60));
        response.addCookie(getCookie("nickname",nickname,10* 60));
        response.addCookie(getCookie("trans",trans,10*60));
        String url = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("url"))
                .map(cookie -> cookie.getValue()).findFirst().orElse(null);

        return "redirect:"+url;

    }
    private Cookie getCookie(String name, String value, int second) {
        Cookie cookie = new Cookie(name,value);
        cookie.setMaxAge(second);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
    @GetMapping("/join")
    @ApiOperation("join 뷰 반환")
    public String join() {
        return "join/join";
    }
    @PostMapping("/join")
    @ApiOperation("회원가입 처리")
    public String joinUser(@RequestBody @Valid JoinDto joinDto) throws IsNotEqualEmailException, SpecialSymbolException, IsNotExistenceEmailContentException, isExistenceUserDataException {
        System.out.println("joinDto.getEmail = " + joinDto.getEmail());
        System.out.println("joinDto. getUserId = " + joinDto.getUserId());
        System.out.println("joinDto.getEmailCode() = " + joinDto.getEmailCode());
        System.out.println("joinDto.getAge() = " + joinDto.getAge());
        System.out.println("joinDto.getPhoneNumber() = " + joinDto.getPhoneNumber());
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .userId(joinDto.getUserId())
                .age(joinDto.getAge())
                .email(joinDto.getEmail())
                .nickname(joinDto.getNickname())
                .trans(joinDto.getTrans())
                .phoneNumber(joinDto.getPhoneNumber())
                .password(joinDto.getPassword()).build();

        String emailData = emailTemplate.emailGetRedisTime("회원가입"+joinDto.getEmail());
        String duplicatePhoneNumberAndNicknameResult = userService.findByPhoneNumberOrNickname(joinDto.getPhoneNumber(), joinDto.getNickname());
        emailValidator.isExistencePhoneNumberOrNickname(duplicatePhoneNumberAndNicknameResult);

        Authority authority = new Authority("ROLE_SITE_USER");
        UserAuthority userAuthority = new UserAuthority(authority);

        TransApi transApi = new TransApi();
        Trans trans = transApi.getTrans(userRequestDto.getTrans());
        SiteMember user = setSiteUser(trans,userRequestDto,userAuthority);
        SiteMember findUser = userService.saveSiteMember(user);

        userAuthority.setUserId(findUser);
        userAuthorityService.saveAuthority(userAuthority);

        return "redirect:/login";
    }
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



}


package com.example.demo.user.email;


import com.example.demo.user.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmailController {
    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;
    @Autowired EmailTemplate emailTemplate;


    @PostMapping("/email/join")
    @ApiOperation("회원가입할 때 필요한 email 전송")
    @ResponseBody
    public String joinRequest(@RequestBody EmailDto email) {
        System.out.println("email = " + email.getEmail());
        emailService.joinMailSend(email);
        return "ok";
    }

    @PostMapping("/email/findId")
    @ApiOperation("userId를 찾을 때 필요한 email 전송")
    @ResponseBody
    public String findIdRequest(@RequestBody EmailDto email) {
        System.out.println("email = " + email.getEmail());
        emailService.checkUserIdSend(email);
        return "ok";
    }

    @PostMapping("/email/changePassword")
    @ApiOperation("비밀번호를 바꾸기 위해 필요한 email 전송")
    @ResponseBody
    public String changePasswordRequest(@RequestBody EmailDto emailDto) {
        System.out.println("email = " + emailDto.getEmail());
        emailService.changePasswordSend(emailDto);
        return "ok";
    }
    @GetMapping("/email/certificate/userId")
    @ApiOperation("userId 조회시 필요한 이메일 데이터 검증")
    @ResponseBody
    public String certificationUserIdEmail(@RequestParam String certificationCode,@RequestParam String email) {
        System.out.println("certificationUserIdEmail - emailTemplate.emailGetRedisTime(\"userId 찾기\"+certificationCode)" + emailTemplate.emailGetRedisTime("userId 찾기"+email));
        if(certificationCode.equals(emailTemplate.emailGetRedisTime("userId 찾기"+email))) {
            return userService.findUserIdByEmail(email);
        }
        return "fail";
    }
    @GetMapping("/email/certificate/password")
    @ApiOperation("password 변경시 필요한 이메일 데이터 검증")
    @ResponseBody
    public String changePasswordEmail(@RequestParam String certificationCode,
                                      @RequestParam String userId,
                                      @RequestParam String changePassword,
                                      @RequestParam String email) {
        System.out.println("changePasswordEmail - emailTemplate.emailGetRedisTime(\"비밀번호 바꾸기\"+ email)" + emailTemplate.emailGetRedisTime("비밀번호 바꾸기"+ email));
        if(certificationCode.equals(emailTemplate.emailGetRedisTime("비밀번호 바꾸기"+ email))) {
            // 비밀번호 변경 로직 실행
            userService.changeUserPasswordByEmailAndUserId(changePassword,email,userId);
            return "ok";
        }
        return "fail";
    }
}


package com.example.demo.email;


import com.example.demo.alarm.AdditionalInformationRedisTemplate;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmailController {
    @Autowired
    EmailService emailService;


    @PostMapping("/email")
    @ApiOperation("EmailDto 기반으로 email 전송")
    @ResponseBody
    public String emailOperate(@RequestBody EmailDto email) {
        System.out.println("email = " + email.getEmail());
        emailService.mailSend(email);
        return "ok";
    }
}


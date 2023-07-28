package com.example.demo.email;


import com.example.demo.alarm.AdditionalInformationRedisTemplate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    @Autowired
    AdditionalInformationRedisTemplate redisTemplate;
    private JavaMailSender mailSender;
    private EmailTemplate emailTemplate;
    private static final String FROM_ADDRESS = "YOUR_EMAIL_ADDRESS";

    public String mailSend(EmailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getEmail());
        message.setFrom(EmailService.FROM_ADDRESS);
        message.setSubject("게시판 프로젝트 이메일 요청");
        String emailAuthenticataionCode = emailTemplate.setRedisTime(mailDto.getEmail());
        message.setText("웹 발신 인증코드: "+ emailAuthenticataionCode);
        mailSender.send(message);
        return emailAuthenticataionCode;
    }
}


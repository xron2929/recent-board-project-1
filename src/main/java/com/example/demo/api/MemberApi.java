package com.example.demo.api;

import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.TokenStatus;
import com.example.demo.security.jwt.UserRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberApi {
    private JwtManager jwtManager;
    @Autowired
    public MemberApi(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    public Boolean checkUserId(String userId, String checkUserId) {
        if(userId.equals(checkUserId)) {
            return true;
        }
        return false;
    }
    public Boolean checkPassword(String refreshToken) throws JsonProcessingException {
        if(jwtManager.validaition(refreshToken).equals(TokenStatus.TIME_SAFE)) {
            return true;
        }
        return false;
    }
    public Boolean checkUserIdAndPassword(String refreshToken,String checkUserId) throws JsonProcessingException {
        UserRequestDto userRequestDto = jwtManager.getUserRequestDto(refreshToken);
        if(!checkUserId(userRequestDto.getUserId(), checkUserId)) {
            return false;
        }
        if(!checkPassword(refreshToken)) {
            return false;
        }
        return true;
    }
}

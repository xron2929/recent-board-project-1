package com.example.demo.security.jwt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
public class RefreshTokenManager {

    public UserRequestDto getUserRequestDto(String refreshToken, Key key) throws JsonProcessingException {
        System.out.println("refreshToken = " + refreshToken);

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key) // 전자서명
                .build() // 생성자
                .parseClaimsJws(refreshToken)
                .getBody();
        System.out.println("claims = " + claims);
        // UserRequestDto userRequestDto = claims.get("userRequestDto", UserRequestDto.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String userRequestDtoJson = claims.get("userRequestDto", String.class);
        UserRequestDto userRequestDto = objectMapper.readValue(userRequestDtoJson, UserRequestDto.class);
        System.out.println("userRequestDto = " + userRequestDto);
        System.out.println("userRequestDto.getUserId() = " + userRequestDto.getUserId());
        System.out.println("userRequestDto.getUserId() = " + userRequestDto.getPassword());
        System.out.println("userRequestDto.getUserId() = " + userRequestDto.getNickname());
        return userRequestDto;
    }

    public boolean validaition(String refreshToken, Key key) throws JsonProcessingException {
        if(refreshToken == null) {
            return false;
        }
        if(refreshToken.isBlank()) {
            log.info("토큰이 없습니다");
            return false;
        }
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
            return true;
            // 컨트롤러에서 처리하는게 나을 것 같아서 나중에 리팩토링
        }  catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}


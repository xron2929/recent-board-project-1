package com.example.demo.jwt;

import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.UserRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@SpringBootTest
@ExtendWith({SpringExtension.class})
@ActiveProfiles(profiles = {"test"})
public class JwtTest {

    @Autowired
    ObjectMapper objectMapper;
    String secretKey;
    Key key;
    @PostConstruct
    private void makeKey() {
        secretKey = "scammedSecretKey-c2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK";
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String setAccessToken(UserRequestDto userRequestDto) throws JsonProcessingException {
        System.out.println("setAccessToken userRequestDto.getPassword() = " + userRequestDto.getPassword());
        Date now = (new Date());
        // 얘를 지금 어떻게 무제한 참조 해결해야됨
        //System.out.println("userRequestDto = " + userRequestDto);

        objectMapper.registerModule(new Hibernate5Module());
        String userRequestDtoJson = objectMapper.writeValueAsString(userRequestDto);
        // 얘만 해결하면 Oauth,일반사용자 < - > JWT 간편하게 해결 가능

        // System.out.println("userRequestDtoJson = " + userRequestDtoJson);
        String jwtToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더에 JWT 타입 나타내는 거인듯ㅗ
                .setIssuedAt(now) // (3) //토큰 발급 시간
                .setExpiration(new Date(now.getTime() + Duration.ofDays(1).toMillis())) // (4) 햔재시간+30분동안
                .claim("userRequestDto", userRequestDtoJson) // (5)
                .signWith(key, SignatureAlgorithm.HS256) // 암호화(압축화)
                .compact(); // 이걸로 response 추가하는듯?
        return jwtToken;
    }

    @Autowired
    JwtManager jwtManager;
    @DisplayName("비회원을 제외한 회원 통신간에서 jwt 사용하는 부분은 조작이 불가능하다")
    @Test
    void canNotScamJwt() throws Exception {

        UserRequestDto userRequestDto = UserRequestDto
                .builder()
                .userId("jwtTest")
                .age(12l)
                .nickname("jwtTestName")
                .email("jwt@gmail.com")
                .phoneNumber("010-8432-0311")
                .trans("남자")
                .password("1234")
                .build();
        String scammedAccessToken = setAccessToken(userRequestDto);
        // when
        Exception duliactionSiteUserException = assertThrows(SignatureException.class, () ->
                jwtManager.getUserRequestDto(scammedAccessToken));
        // then
        Assertions.assertEquals(duliactionSiteUserException.getMessage(),
                "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");

    }
}

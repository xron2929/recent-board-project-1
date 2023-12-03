package com.example.demo.security.jwt;

import com.example.demo.user.authority.Authority;
import com.example.demo.util.cookie.CookieManager;
import com.example.demo.user.userAuthority.UserAuthority;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class JwtManager {
    @Value("${jwt.secretkey}")
    String secretKey;
    Key key;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CookieManager cookieManager;

    @PostConstruct
    private void makeKey() {
        secretKey = "c2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK";
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String getAccessToken(HttpServletRequest request)  {
        if(request.getCookies()== null) {
            System.out.println("JwtManager.getAccessToken - request.getCookies() = null ");
            return null;
        }
        Date now = (new Date());
        String accessToken = Arrays.stream(request.getCookies())
                .filter(cookie ->cookie.getName().equals("accessToken"))
                .map(Cookie::getValue)
                .findFirst().orElse(null);

        return accessToken;
    }
    public String setAccessToken(HttpServletRequest request, HttpServletResponse response,
                                 UserRequestDto userRequestDto) throws JsonProcessingException {
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
        cookieManager.makeSecurityCookie("accessToken",jwtToken,1 * 24 * 60 * 60,response);
        return jwtToken;
    }
    public String setRefreshToken(HttpServletRequest request,HttpServletResponse response,
                                  UserRequestDto userRequestDto) throws JsonProcessingException {
        Date now = (new Date());


        String userRequestDtoJson = objectMapper.writeValueAsString(userRequestDto);
        String jwtToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더에 JWT 타입 나타내는 거인듯
                .setIssuedAt(now) // (3) //토큰 발급 시간
                .setExpiration(new Date(now.getTime() + Duration.ofDays(1).toMillis())) // (4) 햔재시간+30분동안
                .claim("userRequestDto", userRequestDtoJson) // (5)
                .signWith(key, SignatureAlgorithm.HS256) // 암호화(압축화)
                .compact(); // 이걸로 response 추가하는듯?
        System.out.println("jwtManger - setRefreshToken() jwtToken = " + jwtToken);
        cookieManager.makeSecurityCookie("refreshToken",jwtToken,30 * 24 * 60 * 60,response);
        return jwtToken;
    }
    public String getRefreshToken(HttpServletRequest request) throws JsonProcessingException {
        if(request.getCookies()== null) {
            System.out.println("JwtManager.getRefreshToken - request.getCookies() = null ");
            return null;
        }

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie ->cookie.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst().orElse(null);
        return refreshToken;
    }
    public String getAuthorityName(String token) throws JsonProcessingException {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key) // 전자서명
                .build() // 생성자
                .parseClaimsJws(token)
                .getBody();
        System.out.println("claims = " + claims);
        // UserRequestDto userRequestDto = claims.get("userRequestDto", UserRequestDto.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String userRequestDtoJson = claims.get("userRequestDto", String.class);
        UserRequestDto userRequestDto = objectMapper.readValue(userRequestDtoJson, UserRequestDto.class);
        return userRequestDto.getUserAuthorities()
                .stream().map(UserAuthority::getAuthority)
                .map(Authority::getAuthorityName)
                .findFirst().orElse(null);
    }
    public UserRequestDto getUserRequestDto(String token) throws JsonProcessingException {
        System.out.println("UserRequestDto - token = " + token);
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key) // 전자서명
                .build() // 생성자
                .parseClaimsJws(token)
                .getBody();
        System.out.println("claims = " + claims);
        // UserRequestDto userRequestDto = claims.get("userRequestDto", UserRequestDto.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        String userRequestDtoJson = claims.get("userRequestDto", String.class);
        System.out.println("userRequestDtoJson = " + userRequestDtoJson);
        UserRequestDto userRequestDto = objectMapper.readValue(userRequestDtoJson, UserRequestDto.class);
        return userRequestDto;
    }
    public long getLocalDateTimeSecond(String token) throws JsonProcessingException {
        System.out.println("JwtManager - getLocalDateTimeSecond");
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key) // 전자서명
                .build() // 생성자
                .parseClaimsJws(token)
                .getBody();
        long expirationTime = claims.getExpiration().getTime();
        long now = System.currentTimeMillis();
        long remainingSeconds = (expirationTime - now) / 1000;
        System.out.println("JwtManager - remainingSeconds = " + remainingSeconds);
        // UserRequestDto userRequestDto = claims.get("userRequestDto", UserRequestDto.class);
        return remainingSeconds;
    }
    public TokenStatus validation(String token) throws JsonProcessingException {
        System.out.println("JwtManager - validation token " + token);
        if(token == null) {
            return TokenStatus.NONE;
        }
        try{
            if(token.isBlank()) {
                throw new IllegalArgumentException("토큰이 잘못되었습니다");
            }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return TokenStatus.TIME_SAFE;
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
        return TokenStatus.TOKEN_ERROR;
    }


}

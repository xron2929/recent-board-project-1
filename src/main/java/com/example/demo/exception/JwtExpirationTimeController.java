package com.example.demo.exception;


import com.example.demo.cookie.CookieManager;
import com.example.demo.security.authentication.AuthenticationManager;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.RefreshTokenManager;
import com.example.demo.security.jwt.TokenStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class JwtExpirationTimeController {
    @Autowired
    JwtManager jwtManager;
    @Autowired
    AuthenticationManager authenticationManager;
    @Value("${front.domain.url}")
    private String frontDomainUrl;

    @Autowired
    CookieManager cookieManager;
    @Autowired
    RefreshTokenManager refreshTokenManager;
    @GetMapping("/jwt/time")
    @ApiOperation("유저로 로그인 한 경우 남은 시간 알려줌")
    @ResponseBody
    public Long jwtTimeCheck(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        String refreshToken = jwtManager.getRefreshToken(request);
        if(refreshToken == null) {
            return noneUserProcess();
        }
        TokenStatus isSafeJwt = jwtManager.validaition(refreshToken);
        if(isSafeJwt == TokenStatus.TOKEN_ERROR) {
            return jwtExpirationProcess(response);
        }
        long expiredTime = jwtManager.getLocalDateTimeSecond(refreshToken);
        long fiveMinute = 1 * 60 * 1l;
        long finalTime = expiredTime - fiveMinute;
        if(finalTime<=0) {
            return jwtExpirationProcess(response);
        }
        System.out.println("/jwt/time finalTime = " + finalTime);
        return finalTime;
        // 다시 로그인 해야됨
    }
    @GetMapping("/jwt/expiration")
    @ApiOperation("로그인으로 리다이렉트 하고 이전 로그인 정보 삭제")
    public void setJwtManager(HttpServletRequest request, HttpServletResponse response) throws IOException {
        cookieManager.makeZeroSecondCookie("refreshToken",response);
        cookieManager.makeZeroSecondCookie("accessToken",response);
        response.sendRedirect(frontDomainUrl+"/login");
        // response.addHeader("Location", "/login");
        // response.setStatus(302);
        // 다시 로그인 해야됨
    }

    private long jwtExpirationProcess(HttpServletResponse response) {
        System.out.println("/jwt/time finalTime = " + 0);
        return 0l;
    }
    private Long noneUserProcess() {
        System.out.println("/jwt/time finalTime = " + -1);
        return -1l;
    }
}


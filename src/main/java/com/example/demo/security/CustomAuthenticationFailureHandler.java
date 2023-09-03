package com.example.demo.security;

import com.example.demo.alarm.AdditionalInformationRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    AdditionalInformationRedisTemplate additionalInformationRedisTemplate;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof OAuth2AuthenticationException) {
            String errorMessage = ((OAuth2AuthenticationException) exception).getError().getErrorCode();
            String userId = errorMessage.split(", ")[1];
            String url = errorMessage.split(", ")[2];
            additionalInformationRedisTemplate.setOauthUserFirstJoinUrl(userId,url);
            // Pass the exception message as a request attribute
            // Perform the necessary redirect
            response.sendRedirect("/first/oauth/join/"+userId);
        } else {
            // Handle other authentication failure scenarios
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}



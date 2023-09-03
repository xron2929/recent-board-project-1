package com.example.demo.login;


import com.example.demo.user.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class LoginController {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    UserService userService;
    @GetMapping("/login")
    @ApiOperation("login 뷰 반환")
    public String login(@RequestParam(value = "error" , required = false) String error,
                        @RequestParam(value = "exception" , required = false) String exception) {
        log.debug("error={}, exception={}",error,exception);
        System.out.println("auth = "+ SecurityContextHolder.getContext().getAuthentication());
        return "login/login";
    }


    @GetMapping("/logout")
    @ApiOperation("로그아웃 처리")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("authentication = {}" + authentication);
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }


        return "redirect:/login";
    }

}


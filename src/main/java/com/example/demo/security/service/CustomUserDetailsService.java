package com.example.demo.security.service;


import com.example.demo.security.details.PrincipalDetails;
import com.example.demo.security.jwt.JwtManager;
import com.example.demo.security.jwt.UserRequestDto;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.siteuser.SiteMember;
import com.example.demo.boradAndUser.UserJoinRepository;
import com.example.demo.user.UserService;
import com.example.demo.user.userAuthority.UserAuthority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private final UserService userService;
    @Autowired
    JwtManager jwtManager;
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;
    @Autowired
    UserJoinRepository userJoinRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        System.out.println(" ??? username =" + username);
        log.info("load");
        SiteMember user = userService.findBySiteMemberId(username);
        System.out.println("CustomUserDetailsService - loadUserByUsername: user.getPhoneNumber() = " + user.getPhoneNumber());
        System.out.println("CustomUserDetailsService - loadUserByUsername: user.getEmail() = " + user.getEmail());

        user.getUserAuthorities()
                .stream().map(UserAuthority::getAuthority)
                .forEach(authority -> System.out.println("authority.getAuthorityName() = " + authority.getAuthorityName()));


        return createUser(username,user);
    }

    private PrincipalDetails createUser(String username, DefaultMember user) {
        log.info("createUser");
        // PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GrantedAuthority> grantedAuthorities = user.getUserAuthorities().stream()
                .map(userAuthority -> new SimpleGrantedAuthority(userAuthority.getAuthority().getAuthorityName()))
                .collect(Collectors.toList());

        // DefaultMember roleSiteUser = roleProcessService.getRoleMember(user.getId(), "ROLE_SITE_USER");
        Map<String,Object> attributes = new HashMap<>();
        UserRequestDto userRequestDto = UserRequestDto.of((SiteMember) user);
        System.out.println("CustomUserDetailsService - createUser(): userRequestDto = " + userRequestDto);
        System.out.println("CustomUserDetailsService - createUser(): userRequestDto.getPhoneNumber() = " + userRequestDto.getPhoneNumber());

        try {
            String accessToken = jwtManager.setAccessToken(request, response, userRequestDto);
            UserRequestDto findUserRequestDto = jwtManager.getUserRequestDto(accessToken);
            String refreshToken = jwtManager.setRefreshToken(request, response, userRequestDto);
            System.out.println("accessToken = " + accessToken);

            attributes.put("accessToken",accessToken);
            attributes.put("refreshToken",refreshToken);
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        System.out.println("user = " + user);

        user.getUserAuthorities().stream().map(UserAuthority::getAuthority).forEach(authority -> System.out.println(". authority.getAuthorityName() = " + authority.getAuthorityName()));

        return new PrincipalDetails(user,attributes);
    }
}


package com.example.demo.security.details;


import com.example.demo.user.authority.Authority;
import com.example.demo.user.defaultuser.DefaultMember;
import com.example.demo.user.userAuthority.UserAuthority;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
public class PrincipalDetails implements UserDetails, OAuth2User {

    private DefaultMember user;
    private Map<String,Object> attributes;
    @Setter
    private String accessToken;
    private String url;
    public PrincipalDetails(DefaultMember user) {
        this.user = user;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public PrincipalDetails(DefaultMember user, Map<String, Object> attributes, String accessToken) {
        this.user = user;
        this.attributes = attributes;
        this.accessToken = accessToken;
    }

    public PrincipalDetails(DefaultMember user, Map<String, Object> attributes, String accessToken, String url) {
        this.user = user;
        this.attributes = attributes;
        this.accessToken = accessToken;
        this.url = url;
    }

    @Override
    public String getName() {
        // return attributes.get("sub");
        // sub를 리턴하는 역할(사용자 OAUTH ID) 를 해야되나, 안 쓸거라 null 반환할꺼임
        return null;
    }

    public PrincipalDetails(DefaultMember user, Map<String,Object>attributes) {
        this.user = user;
        this.attributes = attributes;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        List<String> collect = user.getUserAuthorities().stream().map(UserAuthority::getAuthority).map(Authority::getAuthorityName)
                .collect(Collectors.toList());
        for(String c: collect) {
            System.out.println("c = " + c);
            GrantedAuthority authority = new SimpleGrantedAuthority(c);
            collection.add(authority);
        };
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication2 = " + authentication1);
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}


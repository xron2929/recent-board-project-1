package com.example.demo.security.provider;

import java.util.Map;

public class GoogleUserInfo implements Oauth2UserInfo{
    private Map<String,Object> attributes;
    public GoogleUserInfo(Map<String,Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
        // sub 개인 키 109742
    }

    @Override
    public String getProvider() {
        return "google";
    }
    // username: provider+_+providerId   ex) google_109742~
    @Override
    public String getEmail() {

        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}

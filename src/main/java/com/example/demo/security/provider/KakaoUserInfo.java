package com.example.demo.security.provider;

import net.minidev.json.JSONObject;

import java.util.Map;

public class KakaoUserInfo implements Oauth2UserInfo{
    private Map<String,Object> attributes;
    public KakaoUserInfo(Map<String,Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
        // sub 개인 키 109742
    }

    @Override
    public String getProvider() {
        return "kakao";
    }
    // username: provider+_+providerId   ex) google_109742~
    @Override
    public String getEmail() {
        JSONObject jsonObject = new JSONObject((Map<String, ?>) attributes.get("kakao_account"));
        return String.valueOf(jsonObject.get("email"));
    }

    @Override
    public String getName() {
        JSONObject jsonObject = new JSONObject((Map<String, ?>) attributes.get("properties"));
        return String.valueOf(jsonObject.get("nickname"));
    }
}

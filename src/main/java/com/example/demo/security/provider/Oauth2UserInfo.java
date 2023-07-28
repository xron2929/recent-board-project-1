package com.example.demo.security.provider;

public interface Oauth2UserInfo {
    String getProviderId(); // facebookId
    String getProvider();
    String getEmail();
    String getName();
}

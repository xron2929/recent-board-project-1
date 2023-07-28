package com.example.demo.user;

import com.example.demo.user.oauthuser.OauthMember;
import com.example.demo.user.siteuser.SiteMember;
import com.example.demo.userAuthority.UserAuthority;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;


@Data
@Getter
public class UserDto implements Serializable {
    private String userId;
    private String password;
    private String email;
    private Long age;
    private String phoneNumber;
    private List<UserAuthority> userAuthorities;
    private String provider;


    public UserDto(String userId, String password, String email,Long age, String phoneNumber, List<UserAuthority> userAuthorities,String provider,String providerId) {
        this.userId = userId;
        this.password = password;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userAuthorities = userAuthorities;
        this.provider = provider;
    }
    public UserDto(String userId, String password, String email,Long age, String phoneNumber) {
        this.userId = userId;
        this.password = password;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public UserDto(String userId, String password, String email,Long age, String phoneNumber, List<UserAuthority> userAuthorities) {
        this.userId = userId;
        this.password = password;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userAuthorities = userAuthorities;
    }
    public static UserDto of(SiteMember user) {
        return new UserDto(user.getUserId(), user.getPassword(), user.getEmail(), user.getAge(), user.getPhoneNumber());
    }

    public static OauthMember from(UserDto userDto) {
        // 복잡하게 들어가면 jwt 처리가 되게 까다로워서 ㅇㅇ..
        return OauthMember
                .builder()
                .userId(userDto.getUserId())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .age(userDto.getAge())
                .phoneNumber(userDto.getPhoneNumber())
                .userAuthorities(userDto.getUserAuthorities())
                .build();
    }


}


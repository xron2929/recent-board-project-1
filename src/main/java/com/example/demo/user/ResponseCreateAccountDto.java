package com.example.demo.user;


import com.example.demo.user.authority.Authority;
import com.example.demo.user.userAuthority.UserAuthority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCreateAccountDto {
    private String userId;
    private String password;
    private String email;
    private Long age;
    private String phoneNumber;
    // private String role;



    public static UserDto from(ResponseCreateAccountDto responseCreateAccountDto,String role) {
        Authority authority = new Authority(role);
        UserAuthority userAuthority = new UserAuthority(authority);
        List<UserAuthority> userAuthorities = new ArrayList<>();
        return new UserDto(responseCreateAccountDto.userId,responseCreateAccountDto.password,responseCreateAccountDto.email, responseCreateAccountDto.age, responseCreateAccountDto.phoneNumber,
                userAuthorities);
    }
}


package com.example.demo.security.jwt;


import com.example.demo.gender.Gender;
import com.example.demo.user.siteuser.SiteMember;
import com.example.demo.userAuthority.UserAuthority;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @Autowired TransApi transApi;
    private String userId; // spring-security의 username
    private Long age; // 나중에 long 으로 바꿀지 Long 할지 결정(DTO 타입말하는 거임)
    private String nickname;
    private String email;
    private String phoneNumber;
    private String trans;
    private String password;
    private LocalDateTime localDateTime;
    private List<UserAuthority> userAuthorities;
    public UserRequestDto(@NonNull String userId,@NonNull Long age,@NonNull String nickname,
                          @NonNull String email, @NonNull String phoneNumber,
                          @NonNull String trans,@NonNull String password) {
        this.userId = userId;
        this.age = age;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.trans = trans;
        this.password = password;
    }

    public UserRequestDto(String userId, Long age, String nickname,
                          String password, LocalDateTime localDateTime) {
        this.userId = userId;
        this.age = age;
        this.nickname = nickname;
        this.password = password;
        this.localDateTime = localDateTime;
    }

    public UserRequestDto(String userId,String password,List<UserAuthority>userAuthorities) {
        this.userId = userId;
        this.password = password;
        this.userAuthorities = userAuthorities;
    }

    public static final SiteMember from(UserRequestDto userRequestDto,Trans trans) {
        return SiteMember
                .builder()
                .userId(userRequestDto.getUserId())
                .password(userRequestDto.getPassword())
                .nickname(userRequestDto.getNickname())
                .age(userRequestDto.getAge())
                .phoneNumber(userRequestDto.getPhoneNumber())
                .email(userRequestDto.getEmail())
                .gender(new Gender(trans))
                .userAuthorities(userRequestDto.getUserAuthorities())
                .build();

    }

    public UserRequestDto(String userId, Long age, String nickname, String password) {
        this.userId = userId;
        this.age = age;
        this.nickname = nickname;
        this.password = password;
    }

    public UserRequestDto(String userId, Long age, String nickname, String email,
                          String phoneNumber, String password,List<UserAuthority>userAuthorities) {
        this.userId = userId;
        this.age = age;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userAuthorities = userAuthorities;
    }
    public static UserRequestDto of(SiteMember user,String emailCode) {
        return new UserRequestDto(
                user.getUserId(),
                user.getAge(),
                user.getNickname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getPassword(),
                user.getUserAuthorities());
    }



}

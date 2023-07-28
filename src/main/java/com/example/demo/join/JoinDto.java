package com.example.demo.join;

import com.example.demo.MyCustomValidation;
import com.example.demo.security.jwt.Trans;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinDto {
    private String nickname;
    private String password;
    @Email(message = "무조건 이메일 형식이어야 합니다.")
    private String email;
    private String emailCode;
    private Long age;
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$",
            message = "000-0000-0000 / 000-000-0000 같은 형식으로 입력해야 합니다.")
    private String phoneNumber;
    private String userId;
    @MyCustomValidation private String trans;
}

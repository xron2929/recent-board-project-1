package com.example.demo.board;

import com.example.demo.user.userAuthority.UserAuthority;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserAuthorityAndUserIdDto {
    private String userId;
    private UserAuthority userAuthority;

    public UserAuthorityAndUserIdDto(String userId, UserAuthority userAuthority) {
        this.userId = userId;
        this.userAuthority = userAuthority;
    }
}

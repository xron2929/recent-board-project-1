package com.example.demo.board;

import com.example.demo.user.userAuthority.UserAuthority;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserAuthorityAndUserIdDto {
    private String userAuthorityName;
    private String userId;

    public UserAuthorityAndUserIdDto(String userAuthorityName, String userId) {
        this.userAuthorityName = userAuthorityName;
        this.userId = userId;
    }
}

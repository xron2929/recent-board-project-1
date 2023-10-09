package com.example.demo.api;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class UserIdAndNickname {
    public String userId;
    public String nickname;

    public UserIdAndNickname(@NonNull String userId,@NonNull String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

}

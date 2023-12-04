package com.example.demo.user;

import lombok.Data;
import lombok.Getter;

@Data
public class UserIdAndIsAllowedAuthorityStatus {
    private String userId;
    private IsAllowedAuthorityStatus isAllowedAuthorityStatus;

    public UserIdAndIsAllowedAuthorityStatus(String userId, IsAllowedAuthorityStatus isAllowedAuthorityStatus) {
        this.userId = userId;
        this.isAllowedAuthorityStatus = isAllowedAuthorityStatus;
    }
}

package com.example.demo.user.api;

public enum CheckStatus {
    SAME_USER(true),
    UN_SAME_USER(false),
    UN_SAME_USER_BUT_ADMIN_TRY(true),
    UN_SAME_USER_AND_UN_ADMIN_TRY(false);



    private Boolean checkMessage;
    CheckStatus(Boolean checkMessage) {
        this.checkMessage = checkMessage;
    }



    public Boolean getCheckMessage() {
        return checkMessage;
    }
}
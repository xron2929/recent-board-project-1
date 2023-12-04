package com.example.demo.user;

public enum IsAllowedAuthorityStatus {
    UN_CHECK_USER_ACCOUNT("게시글 작성자와 게시글 읽는이의 계정이 동일하거나 등급의 위 아래가 확인이 안된 계정"),
    SAME_USER_ACCOUNT("게시글 작성자와 게시글 읽는이가 동일한 계정"),
    UN_SAME_USER_ACCOUNT("게시글 작성자와 게시글 읽는이가 다른 계정"),
    HIGHER_READER_ACCOUNT("게시글 작성자보다 게시글 읽는이가 더 높은 계정");

    private String statusMessage;

    private IsAllowedAuthorityStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }
}

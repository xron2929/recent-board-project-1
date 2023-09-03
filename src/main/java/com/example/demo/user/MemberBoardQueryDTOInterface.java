package com.example.demo.user;

import java.time.LocalDateTime;

public interface MemberBoardQueryDTOInterface {
    Long getBoredId();
    String getTitle();
    String getContents();
    String getUserName();
    LocalDateTime getCreatedDate();
    LocalDateTime getLastModifiedDate();

}

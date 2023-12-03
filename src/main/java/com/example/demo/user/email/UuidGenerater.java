package com.example.demo.user.email;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidGenerater {
    private String uuid;
    public String getUuid() {
        return UUID.randomUUID().toString();
    }
}

package com.example.demo.socket;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    @NonNull
    private String message;
    @NonNull private String message2;
}
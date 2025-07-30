package com.example.messagesystemproject.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {
    private String userId;
    private LocalDateTime dateTime;
    private String textMessage;
}

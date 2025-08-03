package com.example.messagesystemproject.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {
    @Id
    ObjectId messageId;
    private String userId;
    private LocalDateTime dateTime;
    private String textMessage;
}

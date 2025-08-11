package com.example.messagesystemproject.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

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

    private String dialogId;
    private String userId; //отправитель
    @Field("dateTime")
    private LocalDateTime dateTime;
    private String textMessage;

}

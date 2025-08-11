package com.example.messagesystemproject.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "dialogs")
public class Dialog {
    @Id
    private ObjectId dialogId;
    private String dialogTitle;
    private String parentUserId;
    private List<String> usersIdList;
}

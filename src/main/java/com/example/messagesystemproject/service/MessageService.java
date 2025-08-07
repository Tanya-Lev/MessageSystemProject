package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.request.CreateMessageRequest;
import com.example.messagesystemproject.dto.response.CreateMessageResponse;
import com.example.messagesystemproject.dto.response.GetAllMessagesByDialogIdResponse;

import java.util.List;

public interface MessageService {
    public CreateMessageResponse createMessage(String accessToken, CreateMessageRequest request);

    public List<GetAllMessagesByDialogIdResponse> getAllMessagesByDialogId(String accessToken, String id);
}

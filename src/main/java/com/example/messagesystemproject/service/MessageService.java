package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.request.CreateMessageRequest;
import com.example.messagesystemproject.dto.response.CreateMessageResponse;

public interface MessageService {
    public CreateMessageResponse createMessage(String accessToken, CreateMessageRequest request);
}

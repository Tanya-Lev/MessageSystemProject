package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.request.CreateDialogRequest;
import com.example.messagesystemproject.dto.response.CreateDialogResponse;

public interface DialogService {
    public CreateDialogResponse createDialog(String accessToken, CreateDialogRequest request);
}

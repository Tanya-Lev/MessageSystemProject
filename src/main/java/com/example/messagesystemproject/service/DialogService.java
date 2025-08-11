package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.request.CreateDialogRequest;
import com.example.messagesystemproject.dto.response.CreateDialogResponse;
import com.example.messagesystemproject.dto.response.GetAllDialogsByUserIdResponse;

import java.util.List;

public interface DialogService {

    public CreateDialogResponse createDialog(String accessToken, CreateDialogRequest request);

    public List<GetAllDialogsByUserIdResponse> getAllDialogsByUserId(String accessToken);
}

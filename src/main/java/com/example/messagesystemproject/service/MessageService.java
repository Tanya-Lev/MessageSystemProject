package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.request.CreateMessageRequest;
import com.example.messagesystemproject.dto.response.CreateMessageResponse;
import com.example.messagesystemproject.dto.response.GetAllMessagesByDialogIdResponse;
import org.springframework.data.domain.Page;

public interface MessageService {

    public CreateMessageResponse createMessage(String accessToken, CreateMessageRequest request);

    public Page<GetAllMessagesByDialogIdResponse> getAllMessagesByDialogIdWithPaginationAndSort(String accessToken,
                                                                                                String dialogId,
                                                                                                int pageNumber,
                                                                                                int pageSize);

}

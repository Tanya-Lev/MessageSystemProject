package com.example.messagesystemproject.controller;

import com.example.messagesystemproject.dto.request.CreateMessageRequest;
import com.example.messagesystemproject.dto.response.CreateMessageResponse;
import com.example.messagesystemproject.service.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public CreateMessageResponse createDialog(HttpServletRequest httpRequest, @RequestBody CreateMessageRequest request){
        return messageService.createMessage(httpRequest.getHeader("Authorization"), request);
    }
}

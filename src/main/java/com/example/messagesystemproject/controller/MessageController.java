package com.example.messagesystemproject.controller;

import com.example.messagesystemproject.config.AppConstants;
import com.example.messagesystemproject.dto.request.CreateMessageRequest;
import com.example.messagesystemproject.dto.response.CreateMessageResponse;
import com.example.messagesystemproject.dto.response.GetAllMessagesByDialogIdResponse;
import com.example.messagesystemproject.service.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public CreateMessageResponse createMessage(HttpServletRequest httpRequest, @RequestBody @Valid CreateMessageRequest request) {
        return messageService.createMessage(httpRequest.getHeader("Authorization"), request);
    }

    @GetMapping("/getAllMessagesByDialogIdWithPaginationAndSort")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public Page<GetAllMessagesByDialogIdResponse> getAllMessagesByDialogIdWithPaginationAndSort(
            HttpServletRequest httpRequest,
            @RequestParam(name = "dialogId") String dialogId,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER_STR, required = false) int pageNumber,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE_STR, required = false) int pageSize
    ) {
        return messageService.getAllMessagesByDialogIdWithPaginationAndSort(
                httpRequest.getHeader("Authorization"),
                dialogId,
                pageNumber,
                pageSize
        );

    }
}

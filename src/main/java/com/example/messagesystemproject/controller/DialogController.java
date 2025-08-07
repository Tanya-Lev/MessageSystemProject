package com.example.messagesystemproject.controller;

import com.example.messagesystemproject.dto.request.CreateDialogRequest;
import com.example.messagesystemproject.dto.response.CreateDialogResponse;
import com.example.messagesystemproject.dto.response.GetAllDialogsByUserIdResponse;
import com.example.messagesystemproject.service.DialogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dialogs")
public class DialogController {
    @Autowired
    private DialogService dialogService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public CreateDialogResponse createDialog(HttpServletRequest httpRequest, @RequestBody @Valid CreateDialogRequest request){
        return dialogService.createDialog(httpRequest.getHeader("Authorization"), request);
    }

    @GetMapping("/getAllDialogsByUserId")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public List<GetAllDialogsByUserIdResponse> getAllDialogsByUserId(HttpServletRequest httpRequest){
        return dialogService.getAllDialogsByUserId(httpRequest.getHeader("Authorization"));
    }
}

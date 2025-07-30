package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.request.CreateDialogRequest;
import com.example.messagesystemproject.dto.response.CreateDialogResponse;
import com.example.messagesystemproject.entity.Dialog;
import com.example.messagesystemproject.repository.DialogRepository;
import com.example.messagesystemproject.repository.UserRepository;
import com.example.messagesystemproject.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class DialogServiceImpl implements DialogService {
    @Autowired
    private DialogRepository dialogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    public CreateDialogResponse createDialog(String accessToken, CreateDialogRequest request) {

        accessToken = accessToken.substring(7);


        String userId = jwtService.extractClaim(accessToken, claims -> claims.get("id", String.class));
        if (userId == null) {
            throw new RuntimeException("Invalid JWT token: missing user ID");
        }
        if (!userId.equals(request.parentUserId())) {
            throw new RuntimeException("Invalid parent UserId!");
        }
        //во первых в parentUserId передаётся действительно id юзера, который создает диалог
        //todo во вторых что в usersIdList передают существующих юзеров.

        Dialog dialog = Dialog.builder()
                .dialogTitle(request.dialogTitle())
                .parentUserId(request.parentUserId())
                .usersIdList(request.usersIdList())
                .messages(Collections.emptyList())
                .build();

        Dialog savedDialog = dialogRepository.save(dialog);

        return CreateDialogResponse.builder()
                .dialogId(savedDialog.getDialogId().toString())
                .build();
    }
}

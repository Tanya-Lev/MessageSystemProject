package com.example.messagesystemproject.controller;

import com.example.messagesystemproject.dto.request.LoginRequest;
import com.example.messagesystemproject.dto.request.RefreshTokenRequest;
import com.example.messagesystemproject.dto.request.RegisterRequest;
import com.example.messagesystemproject.dto.response.JwtAuthenticationResponse;
import com.example.messagesystemproject.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/registration")
    public JwtAuthenticationResponse registration(@RequestBody @Valid RegisterRequest request) {
        JwtAuthenticationResponse response = authenticationService.register(request);
        return response;
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody @Valid LoginRequest request) {
        JwtAuthenticationResponse response = authenticationService.login(request);
        return response;
    }

    @PostMapping("/refresh-token")
    public JwtAuthenticationResponse refreshToken(@Valid RefreshTokenRequest request) throws IOException {
        return authenticationService.refreshToken(request);
    }
}

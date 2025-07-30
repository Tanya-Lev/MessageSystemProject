package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.request.LoginRequest;
import com.example.messagesystemproject.dto.request.RefreshTokenRequest;
import com.example.messagesystemproject.dto.request.RegisterRequest;
import com.example.messagesystemproject.dto.response.JwtAuthenticationResponse;

import java.io.IOException;

public interface AuthenticationService {

    public JwtAuthenticationResponse register(RegisterRequest request);

    public JwtAuthenticationResponse login(LoginRequest request);

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request) throws IOException;
}

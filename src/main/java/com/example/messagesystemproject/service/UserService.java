package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.response.UserResponse;

public interface UserService {

    public UserResponse whoAmI(String accessToken);
}
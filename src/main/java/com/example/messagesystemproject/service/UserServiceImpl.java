package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.response.UserResponse;
import com.example.messagesystemproject.entity.User;
import com.example.messagesystemproject.repository.DialogRepository;
import com.example.messagesystemproject.repository.UserRepository;
import com.example.messagesystemproject.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DialogRepository dialogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    public UserResponse whoAmI(String accessToken) {
        String userId = jwtService.getUserIdFromToken(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return UserResponse.builder()
                .id(user.getUserId().toString())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .login(user.getLogin())
                .build();
    }
}


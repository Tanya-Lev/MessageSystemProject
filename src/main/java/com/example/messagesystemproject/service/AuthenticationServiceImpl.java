package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.request.LoginRequest;
import com.example.messagesystemproject.dto.request.RefreshTokenRequest;
import com.example.messagesystemproject.dto.request.RegisterRequest;
import com.example.messagesystemproject.dto.response.JwtAuthenticationResponse;
import com.example.messagesystemproject.entity.User;
import com.example.messagesystemproject.repository.UserRepository;
import com.example.messagesystemproject.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Override
    public JwtAuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByLogin(request.login())) {
            throw new IllegalArgumentException("Логин уже используется");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email уже зарегистрирован");
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .avatar(request.avatar())
                .login(request.login())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return new JwtAuthenticationResponse(
                accessToken,
                refreshToken
        );
    }

    @Override
    public JwtAuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.password())
        );

        User user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return new JwtAuthenticationResponse(
                accessToken,
                refreshToken
        );
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request) throws IOException {

        final String userLogin = jwtService.extractLogin(request.refreshToken());
        if (userLogin != null) {
            User user = this.userRepository.findByLogin(userLogin)
                    .orElseThrow();
            if (jwtService.isTokenValid(request.refreshToken(), user)) {
                var accessToken = jwtService.generateToken(user);
                return new JwtAuthenticationResponse(accessToken, request.refreshToken());
            }
        }
        throw new BadCredentialsException("Неправильный Рефреш токен!");
    }

//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractLogin(refreshToken);
//        if (userEmail != null) {
//            var user = this.userRepository.findByEmail(userEmail)
//                    .orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                JwtAuthenticationResponse authResponse = new JwtAuthenticationResponse(accessToken, refreshToken);
//
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }
}


package com.example.messagesystemproject.security;

import com.example.messagesystemproject.entity.User;
import com.example.messagesystemproject.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    //метод, который используется в фильтре авторизации JWT для Spring Security.
    // Он отвечает за то, чтобы при каждом HTTP-запросе проверять наличие и валидность токена,
    // извлекать из него информацию и устанавливать пользователя в контекст безопасности.
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String userLogin;
        try {
            userLogin = jwtService.extractClaim(jwt, Claims::getSubject);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (userLogin != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = User.builder()
                    .login(userLogin)
                    .password("") // не нужен для аутентификации по токену
                    .build();

            if (!jwtService.isTokenValid(jwt, user)) {
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt; //токен
//        final String userLogin;
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        jwt = authHeader.substring(7);
//        userLogin = jwtService.extractLogin(jwt);
//
//        if (userLogin != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            User user = userRepository.findByLogin(userLogin).orElse(null);
//
//            if (user != null && jwtService.isTokenValid(jwt, user)) {
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }

}

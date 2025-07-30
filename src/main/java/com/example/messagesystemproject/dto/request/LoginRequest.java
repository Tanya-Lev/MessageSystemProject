package com.example.messagesystemproject.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на аутентификацию")
public record LoginRequest (
        @Schema(description = "Логин", example = "login")
        @NotBlank(message = "Логин не может быть пустым")
        String login,

        @Schema(description = "Пароль", example = "password")
        @NotBlank(message = "Пароль не может быть пустым")
        String password
){
}

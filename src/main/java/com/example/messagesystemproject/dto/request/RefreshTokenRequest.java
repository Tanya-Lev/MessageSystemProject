package com.example.messagesystemproject.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на создание рефреш токена")
public record RefreshTokenRequest(
        @Schema(example = "sdgdfghdfshsdfh")
        @NotNull
        String refreshToken
) {
}


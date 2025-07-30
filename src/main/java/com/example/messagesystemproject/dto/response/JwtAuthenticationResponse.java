package com.example.messagesystemproject.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record JwtAuthenticationResponse(

        @Schema(example = "1")
        @NotNull
        String accessToken,
        @Schema(example = "1")
        @NotNull
        String refreshToken

) {
}

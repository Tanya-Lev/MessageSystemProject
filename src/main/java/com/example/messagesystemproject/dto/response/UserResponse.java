package com.example.messagesystemproject.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserResponse (
        @Schema(example = "1")
        @NotNull
        String id,

        @Schema(example = "Татьяна")
        @NotNull
        String firstName,

        @Schema(example = "Лев")
        @NotNull
        String lastName,

        @Schema(example = "tanyalev@gmail.com")
        @NotNull
        String email,

        @Schema(example = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6IEDchvJOMioFzENB0n-HzTkF52eKemsCXQ&s")
        @NotNull
        String avatar,

        @Schema(example = "login")
        @NotNull
        String login

) {
}

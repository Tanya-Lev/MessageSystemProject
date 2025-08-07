package com.example.messagesystemproject.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetAllMessagesByDialogIdResponse(
        @Schema(example = "1")
        @NotNull
        String messageId,

        @Schema
        @NotNull
        LocalDateTime dateTime,

        @Schema(example = "Hello!")
        @NotNull
        String textMessage
) {
}

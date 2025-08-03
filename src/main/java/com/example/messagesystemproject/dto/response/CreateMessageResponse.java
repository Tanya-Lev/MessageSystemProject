package com.example.messagesystemproject.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateMessageResponse(
        @Schema(example = "1")
        @NotNull
        String messageId
) {
}

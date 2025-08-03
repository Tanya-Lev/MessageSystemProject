package com.example.messagesystemproject.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на создание сообщения")
public record CreateMessageRequest(
        @Schema(description = "senderId", example = "1")
        @NotBlank(message = "senderId не может быть пустым")
        String senderId,

        @Schema(description = "dialogId", example = "1")
        @NotBlank(message = "dialogId не может быть пустым")
        String dialogId,

        @Schema(description = "textMessage")
        @Size(max = 255, message = "Длина сообщения должна быть не более 255 символов")
        @NotBlank(message = "textMessage не может быть пустым")
        String textMessage
) {
}

package com.example.messagesystemproject.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Запрос на создание диалога")
public record CreateDialogRequest(
        @Schema(description = "dialogTitle")
        @Size(max = 255, message = "Длина названия диалога должна быть не более 255 символов")
        @NotBlank(message = "dialogTitle не может быть пустым")
        String dialogTitle,

        @Schema(description = "parentUserId", example = "1")
        @NotBlank(message = "parentUserId не может быть пустым")
        String parentUserId,

        @Schema(description = "usersIdList")
        @NotNull(message = "usersIdList не может быть null")
        @Size(min = 1, message = "usersIdList должен содержать хотя бы одного пользователя")
        List<String> usersIdList
) {
}



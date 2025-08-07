package com.example.messagesystemproject.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record GetAllDialogsByUserIdResponse(
        @Schema(example = "1")
        @NotNull
        String dialogId,

        @Schema(example = "Название диалога")
        @NotNull
        String dialogTitle,

        @Schema(example = "1")
        @NotNull
        String parentUserId,

        @Schema
        @NotNull
        List<String> usersIdList,

        @Schema
        @NotNull
        String lastMessage
) {
}
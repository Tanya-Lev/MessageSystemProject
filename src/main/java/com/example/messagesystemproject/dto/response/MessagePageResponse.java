package com.example.messagesystemproject.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record MessagePageResponse(//обёртка для Page чтоб красиво выводить

        List<GetAllMessagesByDialogIdResponse> messagesResponse,

        Integer pageNumber,

        Integer pageSize,

        int totalElements,

        int totalPages,

        boolean isLast
) {
}

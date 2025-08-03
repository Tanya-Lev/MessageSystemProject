package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.request.CreateMessageRequest;
import com.example.messagesystemproject.dto.response.CreateMessageResponse;
import com.example.messagesystemproject.entity.Dialog;
import com.example.messagesystemproject.entity.Message;
import com.example.messagesystemproject.repository.DialogRepository;
import com.example.messagesystemproject.repository.UserRepository;
import com.example.messagesystemproject.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final DialogRepository dialogRepository;

    private final UserRepository userRepository;

    private final JwtService jwtService;;

    @Override
    public CreateMessageResponse createMessage(String accessToken, CreateMessageRequest request) {
        // Удаляем префикс Bearer из токена (stripBearerPrefix) и извлекаем userId.
        String userId = jwtService.getUserIdFromToken(accessToken);

        // Проверяем, что senderId из тела запроса совпадает с userId из токена.
        if (!userId.equals(request.senderId())) {
            throw new RuntimeException("Invalid senderId!");
        }

        // Ищем в базе диалог по dialogId.
        Dialog dialog = dialogRepository.findById(request.dialogId())
                .orElseThrow(() -> new RuntimeException("Диалог не найден"));

        Message message = Message.builder()
                .messageId(new ObjectId())
                .userId(userId)
                .dateTime(LocalDateTime.now())
                .textMessage(request.textMessage())
                .build();

        // Добавляем созданное сообщение в диалог.
        dialog.addMessage(message);
        dialogRepository.save(dialog);

        return new CreateMessageResponse(message.getMessageId().toString());
    }
}

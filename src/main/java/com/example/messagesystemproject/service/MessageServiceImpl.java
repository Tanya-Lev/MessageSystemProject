package com.example.messagesystemproject.service;

import com.example.messagesystemproject.config.AppConstants;
import com.example.messagesystemproject.dto.request.CreateMessageRequest;
import com.example.messagesystemproject.dto.response.CreateMessageResponse;
import com.example.messagesystemproject.dto.response.GetAllMessagesByDialogIdResponse;
import com.example.messagesystemproject.entity.Dialog;
import com.example.messagesystemproject.entity.Message;
import com.example.messagesystemproject.repository.DialogRepository;
import com.example.messagesystemproject.repository.MessageRepository;
import com.example.messagesystemproject.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final DialogRepository dialogRepository;

    private final MessageRepository messageRepository;

    private final JwtService jwtService;

    @Override
    public CreateMessageResponse createMessage(String accessToken, CreateMessageRequest request) {
        // Удаляем префикс Bearer из токена (stripBearerPrefix) и извлекаем userId.
        String userId = jwtService.getUserIdFromToken(accessToken);

        // Проверяем, что senderId из тела запроса совпадает с userId из токена.
        if (!userId.equals(request.senderId())) {
            throw new RuntimeException("Неверный отправитель senderId!");
        }

        // Ищем в базе диалог по dialogId.
        Dialog dialog = dialogRepository.findById(request.dialogId())
                .orElseThrow(() -> new RuntimeException("Диалог не найден"));

        // Проверяем , что пользователь является участником диалога.
        if (!dialog.getUsersIdList().contains(userId)) {
            throw new RuntimeException("Пользователь не участник диалога");
        }

        Message message = Message.builder()
                .dialogId(request.dialogId())
                .userId(userId)
                .dateTime(LocalDateTime.now())
                .textMessage(request.textMessage())
                .build();

        Message savedMessage = messageRepository.save(message);

        return CreateMessageResponse.builder()
                .messageId(savedMessage.getMessageId().toString())
                .build();
    }

    @Override
    public Page<GetAllMessagesByDialogIdResponse> getAllMessagesByDialogIdWithPaginationAndSort(String accessToken,
                                                                                                String dialogId,
                                                                                                int pageNumber,
                                                                                                int pageSize) {

        // 1. Проверяем, что пользователь действительно является участником диалога (права доступа)
        String userId = jwtService.getUserIdFromToken(accessToken);
        boolean hasAccess = dialogRepository.existsByDialogIdAndUsersIdListContaining(dialogId, userId);

        if (!hasAccess) {
            throw new RuntimeException("Доступ к диалогу запрещён");
        }

        // 2. Проверяем, что сам диалог существует, иначе кидаем ошибку
        dialogRepository.findById(dialogId)
                .orElseThrow(() -> new RuntimeException("Диалог не найден"));//todo написать ручками реализацию existsByDialogIdAndUsersIdListContaining метода чтоб 2 раза не ходить в БД

        // 3. Валидация параметров пагинации:
        //    - если page < 0 — используем дефолтный номер страницы
        //    - если size <= 0 или больше максимально допустимого — используем дефолтный размер страницы
        int validPage = pageNumber < 0 ? AppConstants.DEFAULT_PAGE_NUMBER : pageNumber;
        int validSize = (pageSize <= 0 || pageSize > AppConstants.MAX_PAGE_SIZE) ? AppConstants.DEFAULT_PAGE_SIZE : pageSize;


        // 4. Создаём Pageable с проверенными значениями + сортировка по дате в порядке убывания
        Pageable pageable = PageRequest.of(validPage, validSize, Sort.by(Sort.Direction.DESC, "dateTime"));

        // 5. Получаем страницу сообщений из репозитория по dialogId
        Page<Message> messagePage = messageRepository.findByDialogId(dialogId, pageable);


        return messagePage.map(message -> GetAllMessagesByDialogIdResponse.builder()
                .messageId(message.getMessageId().toString())
                .dateTime(message.getDateTime())
                .textMessage(message.getTextMessage())
                .build()
        );
    }

    //todo вернуть getAllMessagesByDialogId()
//    @Override
//    public List<GetAllMessagesByDialogIdResponse> getAllMessagesByDialogId(String accessToken, String id) {
//
//        Dialog dialog = dialogRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Диалог не найден"));
//
//        List<Message> messageList = dialog.getMessages();
//
//        messageList.sort(Comparator.comparing(Message::getDateTime).reversed());
//
//        List<GetAllMessagesByDialogIdResponse> responseList = new ArrayList<>();
//        for (Message message : messageList) {
//            GetAllMessagesByDialogIdResponse response = GetAllMessagesByDialogIdResponse.builder()
//                    .messageId(message.getMessageId().toString())
//                    .dateTime(message.getDateTime())
//                    .textMessage(message.getTextMessage())
//                    .build();
//            responseList.add(response);
//        }
//
//        return responseList;
//    }

}

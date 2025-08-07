package com.example.messagesystemproject.service;

import com.example.messagesystemproject.dto.request.CreateDialogRequest;
import com.example.messagesystemproject.dto.response.CreateDialogResponse;
import com.example.messagesystemproject.dto.response.GetAllDialogsByUserIdResponse;
import com.example.messagesystemproject.entity.Dialog;
import com.example.messagesystemproject.repository.DialogRepository;
import com.example.messagesystemproject.repository.UserRepository;
import com.example.messagesystemproject.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DialogServiceImpl implements DialogService {

    private final DialogRepository dialogRepository;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Override
    public CreateDialogResponse createDialog(String accessToken, CreateDialogRequest request) {
        String userId = jwtService.getUserIdFromToken(accessToken);

        // Проверка, существует ли пользователь-создатель
        if (!userId.equals(request.parentUserId())) {
            throw new RuntimeException("Invalid parent UserId!");
        }

        // Копия списка участников
        List<String> usersIdList = new ArrayList<>(request.usersIdList());

        // Проверка, существуют ли все участники
        List<String> missingUsersId = usersIdList.stream()
                .filter(id -> !userRepository.existsById(id))// отфильтровываем только те ID, которых нет в базе (ищем отсутствующих пользователей)
                .toList();// список ID, которые отсутствуют в базе.

        // Если список отсутствующих пользователей не пустой — значит, кто-то передал ID, которых не существует в системе
        if (!missingUsersId.isEmpty()) {
            throw new IllegalArgumentException("Некоторые пользователи не найдены: " + missingUsersId);
        }

        // Добавляем создателя диалога в список участников
        usersIdList.add(request.parentUserId());

        Dialog dialog = Dialog.builder()
                .dialogTitle(request.dialogTitle())
                .parentUserId(request.parentUserId())
                .usersIdList(usersIdList)
                .messages(Collections.emptyList())
                .build();

        Dialog savedDialog = dialogRepository.save(dialog);

        return CreateDialogResponse.builder()
                .dialogId(savedDialog.getDialogId().toString())
                .build();
    }

    @Override
    public List<GetAllDialogsByUserIdResponse> getAllDialogsByUserId(String accessToken) {
        String userId = jwtService.getUserIdFromToken(accessToken);

        List<Dialog> dialogList = dialogRepository.findAllByUsersIdListContaining(userId);

        List<GetAllDialogsByUserIdResponse> responseList = new ArrayList<>();

        for (Dialog dialog : dialogList) {
            String lastMessage = dialog.getMessages().isEmpty()
                    ? "Переписка пуста!"  // или null — в зависимости от требований
                    : dialog.getMessages().getLast().getTextMessage();

            GetAllDialogsByUserIdResponse response = GetAllDialogsByUserIdResponse.builder()
                    .dialogId(dialog.getDialogId().toString())
                    .dialogTitle(dialog.getDialogTitle())
                    .parentUserId(dialog.getParentUserId())
                    .usersIdList(dialog.getUsersIdList())
                    .lastMessage(lastMessage)
                    .build();

            responseList.add(response);
        }
        return responseList;
    }
}

package com.example.messagesystemproject.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на регистрацию")
public record RegisterRequest(
        @Schema(description = "Имя пользователя", example = "Татьяна")
        @NotBlank(message = "Имя пользователя не может быть пустым")
        String firstName,

        @Schema(description = "Фамилия пользователя", example = "Лев")
        @NotBlank(message = "Фамилия пользователя не может быть пустой")
        String lastName,

        @Schema(description = "URL или имя файла аватара", example = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6IEDchvJOMioFzENB0n-HzTkF52eKemsCXQ&s", nullable = true)
        String avatar,

        @Schema(description = "Адрес электронной почты", example = "tanyalev@gmail.com")
        @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
        @NotBlank(message = "Адрес электронной почты не может быть пустыми")
        @Email(message = "Email адрес должен быть в формате user@example.com")
        String email,

        @Schema(description = "Логин", example = "login")
        @Size(max = 255, message = "Длина логина должна быть не более 255 символов")
        @NotNull
        String login,

        @Schema(description = "Пароль", example = "password")
        @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
        @NotNull
        String password
        ) {
}

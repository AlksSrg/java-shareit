package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для создания нового пользователя.
 * Содержит валидационные аннотации для обязательных полей.
 *
 * @param name  имя пользователя
 * @param email email пользователя
 */
public record UserCreateRequestDto(
        @NotEmpty(message = "Имя пользователя не может быть пустым")
        String name,

        @NotNull(message = "Email не может быть пустым")
        @Email(message = "Некорректный формат email")
        String email
) {

    /**
     * Преобразует DTO создания в базовый DTO.
     *
     * @return базовый DTO пользователя
     */
    public UserBaseDto toBaseDto() {
        return new UserBaseDto(name, email);
    }
}
package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для создания нового пользователя.
 * Содержит валидационные аннотации для обязательных полей.
 */
public record UserCreateRequestDto(
        @NotEmpty String name,
        @NotNull @Email String email
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
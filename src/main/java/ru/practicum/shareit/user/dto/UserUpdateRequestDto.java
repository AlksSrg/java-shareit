package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;

/**
 * DTO для обновления пользователя.
 * Все поля опциональны для частичного обновления.
 */
public record UserUpdateRequestDto(
        String name,
        @Email String email
) {

    /**
     * Преобразует DTO обновления в базовый DTO.
     *
     * @return базовый DTO пользователя
     */
    public UserBaseDto toBaseDto() {
        return new UserBaseDto(name, email);
    }
}
package ru.practicum.shareit.server.user.dto;

/**
 * DTO для создания нового пользователя на сервере.
 * Содержит данные для создания пользователя без валидационных аннотаций.
 */

public record UserCreateRequestDto(
        String name,
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
package ru.practicum.shareit.server.user.dto;

/**
 * DTO для ответа с информацией о пользователе на сервере.
 * Содержит полную информацию о пользователе включая идентификатор.
 */

public record UserResponseDto(
        Long id,
        String name,
        String email
) {

    /**
     * Преобразует DTO ответа в базовый DTO.
     *
     * @return базовый DTO пользователя
     */
    public UserBaseDto toBaseDto() {
        return new UserBaseDto(name, email);
    }
}
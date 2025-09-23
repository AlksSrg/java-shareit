package ru.practicum.shareit.server.user.dto;

/**
 * DTO для обновления пользователя на сервере.
 * Все поля опциональны для частичного обновления.
 */

public record UserUpdateRequestDto(
        String name,
        String email
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
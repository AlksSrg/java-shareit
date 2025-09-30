package ru.practicum.shareit.user.dto;

/**
 * DTO для ответа с информацией о пользователе.
 * Содержит полную информацию о пользователе включая идентификатор.
 *
 * @param id    идентификатор пользователя
 * @param name  имя пользователя
 * @param email email пользователя
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
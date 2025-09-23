package ru.practicum.shareit.gateway.user.dto;

/**
 * Базовый DTO для работы с пользователем.
 * Содержит основные поля пользователя без идентификатора.
 */
public record UserBaseDto(String name, String email) {
    /**
     * Конструктор базового DTO пользователя.
     *
     * @param name  имя пользователя
     * @param email email пользователя
     */
    public UserBaseDto {
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return имя пользователя
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * Возвращает email пользователя.
     *
     * @return email пользователя
     */
    @Override
    public String email() {
        return email;
    }
}
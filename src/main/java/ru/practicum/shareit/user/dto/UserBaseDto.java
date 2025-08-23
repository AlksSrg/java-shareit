package ru.practicum.shareit.user.dto;

/**
 * Базовый DTO для работы с пользователем.
 * Содержит основные поля пользователя без идентификатора.
 */
public class UserBaseDto {
    private final String name;
    private final String email;

    /**
     * Конструктор базового DTO пользователя.
     *
     * @param name  имя пользователя
     * @param email email пользователя
     */
    public UserBaseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }
}
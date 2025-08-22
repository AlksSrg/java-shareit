package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель пользователя системы.
 * Содержит основную информацию о пользователе:
 * идентификатор, логин или имя, email.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Уникальный идентификатор пользователя
     */
    private Long id;

    /**
     * Имя или логин пользователя
     */
    private String name;

    /**
     * Адрес электронной почты
     */
    private String email;
}
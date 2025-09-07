package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "users")
public class User {

    /**
     * Уникальный идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя или логин пользователя
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Адрес электронной почты
     */
    @Column(name = "email", nullable = false, unique = true, length = 512)
    private String email;
}
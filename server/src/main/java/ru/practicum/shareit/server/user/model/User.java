package ru.practicum.shareit.server.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель пользователя системы.
 * Содержит основную информацию о пользователе:
 * идентификатор, имя, email.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя. Обязательное поле.
     */
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(max = 255, message = "Имя пользователя не может превышать 255 символов")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Адрес электронной почты.
     */
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть валидным адресом")
    @Column(name = "email", nullable = false, unique = true, length = 512)
    private String email;
}
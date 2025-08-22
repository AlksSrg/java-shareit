package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Модель запроса вещи.
 * Содержит основную информацию о запросе:
 * идентификатор, текст запроса, пользователь {@link User} сделавший запрос, дату и время создания запроса.
 */

@Data
public class ItemRequest {

    /**
     * Уникальный идентификатор запроса
     */
    private Long id;

    /**
     * Текст запроса, содержащий описание требуемой вещи
     */
    private String description;

    /**
     * Пользователь, создавший запрос
     */
    private User requestor;

    /**
     * Дата и время создания запроса
     */
    private LocalDateTime created;
}
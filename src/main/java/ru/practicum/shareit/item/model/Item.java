package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

/**
 * Модель вещи для аренды.
 * Содержит основную информацию о вещи:
 * идентификатор, краткое название, описание, статус доступности, владелец вещи {@link User}, ссылка на запрос вещи.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    /**
     * Уникальный идентификатор вещи
     */
    private Long id;

    /**
     * Краткое название вещи
     */
    private String name;

    /**
     * Развёрнутое описание
     */
    private String description;

    /**
     * Статус о том, доступна или нет вещь для аренды
     */
    private Boolean available;

    /**
     * Владелец вещи
     */
    private User owner;

    /**
     * Если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос
     */
    private Long requestId;
}
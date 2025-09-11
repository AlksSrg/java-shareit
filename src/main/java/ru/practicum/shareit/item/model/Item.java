package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "items")
public class Item {

    /**
     * Уникальный идентификатор вещи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Краткое название вещи.
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Развёрнутое описание.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Статус о том, доступна или нет вещь для аренды.
     */
    @Column(name = "available", nullable = false)
    private Boolean available;

    /**
     * Владелец вещи.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /**
     * Если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.
     */
    @Column(name = "request_id")
    private Long requestId;
}
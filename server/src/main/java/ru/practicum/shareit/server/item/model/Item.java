package ru.practicum.shareit.server.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.user.model.User;

/**
 * Модель вещи для аренды.
 * Содержит основную информацию о вещи:
 * идентификатор, название, описание, статус доступности, владелец вещи, ссылка на запрос вещи.
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
     * Название вещи.
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Описание вещи.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Статус доступности вещи для аренды.
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
     * Идентификатор запроса, если вещь была создана по запросу.
     */
    @Column(name = "request_id")
    private Long requestId;
}
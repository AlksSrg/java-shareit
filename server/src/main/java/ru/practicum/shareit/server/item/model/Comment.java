package ru.practicum.shareit.server.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;

/**
 * Модель отзыва о вещи.
 * Содержит информацию об отзыве: текст, автор, вещь и дата создания.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    /**
     * Уникальный идентификатор отзыва.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текст отзыва.
     */
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    /**
     * Вещь, к которой оставлен отзыв.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /**
     * Автор отзыва.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * Дата создания отзыва.
     */
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
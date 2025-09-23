package ru.practicum.shareit.server.request.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;

/**
 * Модель запроса вещи.
 * Содержит основную информацию о запросе:
 * идентификатор, текст запроса, пользователь сделавший запрос, дату и время создания запроса.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {

    /**
     * Уникальный идентификатор запроса.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текст запроса, содержащий описание требуемой вещи.
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Пользователь, создавший запрос.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    /**
     * Дата и время создания запроса.
     */
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
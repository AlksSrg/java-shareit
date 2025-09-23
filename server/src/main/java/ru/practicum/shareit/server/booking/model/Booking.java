package ru.practicum.shareit.server.booking.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;

/**
 * Модель бронирования вещи.
 * Содержит основную информацию о бронировании:
 * идентификатор, дата и время начала бронирования, дата и время конца бронирования,
 * вещь, которую пользователь бронирует,
 * пользователь, который осуществляет бронирование,
 * статус бронирования.
 * Поддерживает валидацию всех обязательных полей.
 */
@Data
@Entity
@Table(name = "bookings")
public class Booking {

    /**
     * Уникальный идентификатор бронирования
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата и время начала бронирования
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    /**
     * Дата и время конца бронирования
     */
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    /**
     * Вещь, которую пользователь бронирует
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /**
     * Пользователь, который осуществляет бронирование
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    /**
     * Статус бронирования
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BookingStatus status;
}
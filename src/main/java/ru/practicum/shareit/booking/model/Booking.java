package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Модель бронирования вещи.
 * Содержит основную информацию о бронировании:
 * идентификатор, дата и время начала бронирования, дата и время конца бронирования,
 * вещь{@link Item}, которую пользователь{@link User} бронирует,
 * пользователь{@link User}, который осуществляет бронирование,
 * статус бронирования {@link BookingStatus}.
 * Поддерживает валидацию всех обязательных полей.
 */

@Data
public class Booking {

    /**
     * Уникальный идентификатор бронирования
     */
    private Long id;

    /**
     * Дата и время начала бронирования
     */
    private LocalDateTime start;

    /**
     * Дата и время конца бронирования
     */
    private LocalDateTime end;

    /**
     * Вещь, которую пользователь бронирует
     */
    private Item item;

    /**
     * Пользователь, который осуществляет бронирование
     */
    private User booker;

    /**
     * Cтатус бронирования
     */
    private BookingStatus status;

}
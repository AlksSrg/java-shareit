package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

/**
 * DTO для создания нового бронирования на сервере.
 * Содержит данные для создания бронирования без валидационных аннотаций.
 *
 * @param itemId идентификатор вещи
 * @param start  дата и время начала бронирования
 * @param end    дата и время окончания бронирования
 */
public record BookingCreateRequestDto(
        Long itemId,
        LocalDateTime start,
        LocalDateTime end
) {
}
package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о бронировании.
 * Содержит полную информацию о бронировании, включая данные о вещи и пользователе.
 */

@Builder
public record BookingResponseDto(
        /**
         * Уникальный идентификатор бронирования
         */
        Long id,

        /**
         * Дата и время начала бронирования
         */
        LocalDateTime start,

        /**
         * Дата и время окончания бронирования
         */
        LocalDateTime end,

        /**
         * Статус бронирования
         */
        BookingStatus status,

        /**
         * Информация о забронированной вещи
         */
        ItemResponseDto item,

        /**
         * Информация о пользователе, который осуществил бронирование
         */
        UserResponseDto booker
) {
}
package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о бронировании.
 * Содержит полную информацию о бронировании включая связанные сущности.
 */
public record BookingResponseDto(
        Long id,
        LocalDateTime start,
        LocalDateTime end,
        BookingStatus status,
        UserResponseDto booker,
        ItemResponseDto item
) {
}
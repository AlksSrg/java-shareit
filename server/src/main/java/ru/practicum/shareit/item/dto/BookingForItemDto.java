package ru.practicum.shareit.item.dto;

/**
 * DTO для отображения информации о бронировании в контексте вещи.
 * Содержит минимальную информацию о бронировании для отображения в составе информации о вещи.
 */

public record BookingForItemDto(
        Long id,
        Long bookerId
) {
}
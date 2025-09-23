package ru.practicum.shareit.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для отображения информации о бронировании в контексте вещи.
 * Содержит минимальную информацию о бронировании для отображения в составе информации о вещи.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingForItemDto {
    private Long id;
    private Long bookerId;
}
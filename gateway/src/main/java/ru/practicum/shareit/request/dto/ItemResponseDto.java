package ru.practicum.shareit.request.dto;

import lombok.Builder;

/**
 * DTO для ответа с информацией о вещи.
 * Содержит основные данные о вещи для отображения в ответах на запросы.
 */

@Builder
public record ItemResponseDto(
        Long id,
        String name,
        String description,
        Boolean available,
        Long requestId
) {
}
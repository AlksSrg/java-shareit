package ru.practicum.shareit.item.dto;

/**
 * DTO для обновления вещи.
 * Все поля опциональны для частичного обновления.
 * Используется для передачи данных от клиента к серверу при обновлении вещи.
 */
public record ItemUpdateRequestDto(
        String name,
        String description,
        Boolean available
) {
}
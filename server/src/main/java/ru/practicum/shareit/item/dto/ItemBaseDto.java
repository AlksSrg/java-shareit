package ru.practicum.shareit.item.dto;

/**
 * Базовый DTO для работы с вещью.
 * Содержит основные поля вещи без идентификатора и владельца.
 */

public record ItemBaseDto(
        String name,
        String description,
        Boolean available,
        Long requestId
) {
}
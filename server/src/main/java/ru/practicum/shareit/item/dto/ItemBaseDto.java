package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Базовый DTO для работы с вещью.
 * Содержит основные поля вещи без идентификатора и владельца.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemBaseDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
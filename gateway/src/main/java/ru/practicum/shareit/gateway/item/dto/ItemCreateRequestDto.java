package ru.practicum.shareit.gateway.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для создания новой вещи.
 * Содержит валидационные аннотации для обязательных полей.
 * Используется для передачи данных от клиента к серверу при создании вещи.
 */
public record ItemCreateRequestDto(
        @NotEmpty(message = "Название вещи не может быть пустым")
        String name,

        @NotEmpty(message = "Описание вещи не может быть пустым")
        String description,

        @NotNull(message = "Статус доступности не может быть пустым")
        Boolean available,

        Long requestId
) {
}
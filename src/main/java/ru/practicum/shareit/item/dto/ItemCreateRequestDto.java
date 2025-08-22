package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO для создания новой вещи.
 * Содержит валидационные аннотации для обязательных полей.
 */
public record ItemCreateRequestDto(
        @NotEmpty String name,
        @NotEmpty String description,
        @NotNull Boolean available,
        Long requestId
) {

    /**
     * Преобразует DTO создания в базовый DTO.
     *
     * @return базовый DTO вещи
     */
    public ItemBaseDto toBaseDto() {
        return new ItemBaseDto(name, description, available, requestId);
    }
}
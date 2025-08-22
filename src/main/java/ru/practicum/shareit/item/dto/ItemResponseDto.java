package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.user.dto.UserResponseDto;

/**
 * DTO для ответа с информацией о вещи.
 * Содержит полную информацию о вещи включая владельца.
 */
public record ItemResponseDto(
        Long id,
        String name,
        String description,
        Boolean available,
        UserResponseDto owner,
        Long requestId
) {

    /**
     * Преобразует DTO ответа в базовый DTO.
     *
     * @return базовый DTO вещи
     */
    public ItemBaseDto toBaseDto() {
        return new ItemBaseDto(name, description, available, requestId);
    }
}
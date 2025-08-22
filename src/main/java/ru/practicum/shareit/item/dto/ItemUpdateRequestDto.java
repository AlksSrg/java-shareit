package ru.practicum.shareit.item.dto;

/**
 * DTO для обновления вещи.
 * Все поля опциональны для частичного обновления.
 */
public record ItemUpdateRequestDto(
        String name,
        String description,
        Boolean available
) {

    /**
     * Преобразует DTO обновления в базовый DTO.
     *
     * @return базовый DTO вещи
     */
    public ItemBaseDto toBaseDto() {
        return new ItemBaseDto(name, description, available, null);
    }
}
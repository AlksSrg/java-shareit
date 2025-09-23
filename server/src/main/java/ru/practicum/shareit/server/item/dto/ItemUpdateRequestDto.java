package ru.practicum.shareit.server.item.dto;

/**
 * DTO для обновления вещи на сервере.
 * Все поля опциональны для частичного обновления.
 *
 * @param name        новое название вещи (опционально)
 * @param description новое описание вещи (опционально)
 * @param available   новый статус доступности вещи (опционально)
 */
public record ItemUpdateRequestDto(
        String name,
        String description,
        Boolean available
) {
}
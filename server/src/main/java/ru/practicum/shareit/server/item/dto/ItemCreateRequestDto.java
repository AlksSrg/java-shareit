package ru.practicum.shareit.server.item.dto;

/**
 * DTO для создания новой вещи на сервере.
 * Содержит данные для создания вещи без валидационных аннотаций.
 *
 * @param name        название вещи
 * @param description описание вещи
 * @param available   статус доступности вещи
 * @param requestId   идентификатор запроса (опционально)
 */
public record ItemCreateRequestDto(
        String name,
        String description,
        Boolean available,
        Long requestId
) {
}
package ru.practicum.shareit.item.dto;

/**
 * Базовый DTO для работы с вещью.
 * Содержит основные поля вещи без идентификатора и владельца.
 */
public class ItemBaseDto {
    private final String name;
    private final String description;
    private final Boolean available;
    private final Long requestId;

    /**
     * Конструктор базового DTO вещи.
     *
     * @param name        название вещи
     * @param description описание вещи
     * @param available   статус доступности
     * @param requestId   идентификатор запроса
     */
    public ItemBaseDto(String name, String description, Boolean available, Long requestId) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Boolean available() {
        return available;
    }

    public Long requestId() {
        return requestId;
    }
}
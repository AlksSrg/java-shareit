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

    /**
     * Возвращает название вещи.
     *
     * @return название вещи
     */
    public String name() {
        return name;
    }

    /**
     * Возвращает описание вещи.
     *
     * @return описание вещи
     */
    public String description() {
        return description;
    }

    /**
     * Возвращает статус доступности вещи.
     *
     * @return true если вещь доступна для бронирования
     */
    public Boolean available() {
        return available;
    }

    /**
     * Возвращает идентификатор запроса.
     *
     * @return идентификатор запроса или null если вещь создана не по запросу
     */
    public Long requestId() {
        return requestId;
    }
}
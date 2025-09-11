package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

/**
 * DTO для ответа с информацией о вещи.
 * Содержит полную информацию о вещи включая владельца.
 */
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserResponseDto owner;
    private Long requestId;
    private List<CommentResponseDto> comments;
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;

    /**
     * Конструктор по умолчанию.
     */
    public ItemResponseDto() {
    }

    /**
     * Конструктор с параметрами.
     *
     * @param id          идентификатор вещи
     * @param name        название вещи
     * @param description описание вещи
     * @param available   статус доступности
     * @param owner       владелец вещи
     * @param requestId   идентификатор запроса
     * @param comments    список комментариев
     * @param lastBooking последнее бронирование
     * @param nextBooking следующее бронирование
     */
    public ItemResponseDto(Long id, String name, String description, Boolean available,
                           UserResponseDto owner, Long requestId, List<CommentResponseDto> comments,
                           BookingForItemDto lastBooking, BookingForItemDto nextBooking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requestId = requestId;
        this.comments = comments;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }

    /**
     * Возвращает идентификатор вещи.
     *
     * @return идентификатор вещи
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор вещи.
     *
     * @param id идентификатор вещи
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает название вещи.
     *
     * @return название вещи
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название вещи.
     *
     * @param name название вещи
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает описание вещи.
     *
     * @return описание вещи
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание вещи.
     *
     * @param description описание вещи
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Возвращает статус доступности вещи.
     *
     * @return true если вещь доступна для бронирования
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * Устанавливает статус доступности вещи.
     *
     * @param available статус доступности
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * Возвращает владельца вещи.
     *
     * @return DTO владельца вещи
     */
    public UserResponseDto getOwner() {
        return owner;
    }

    /**
     * Устанавливает владельца вещи.
     *
     * @param owner DTO владельца вещи
     */
    public void setOwner(UserResponseDto owner) {
        this.owner = owner;
    }

    /**
     * Возвращает идентификатор запроса.
     *
     * @return идентификатор запроса или null если вещь создана не по запросу
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * Устанавливает идентификатор запроса.
     *
     * @param requestId идентификатор запроса
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * Возвращает список комментариев к вещи.
     *
     * @return список комментариев
     */
    public List<CommentResponseDto> getComments() {
        return comments;
    }

    /**
     * Устанавливает список комментариев к вещи.
     *
     * @param comments список комментариев
     */
    public void setComments(List<CommentResponseDto> comments) {
        this.comments = comments;
    }

    /**
     * Возвращает информацию о последнем бронировании.
     *
     * @return DTO последнего бронирования
     */
    public BookingForItemDto getLastBooking() {
        return lastBooking;
    }

    /**
     * Устанавливает информацию о последнем бронировании.
     *
     * @param lastBooking DTO последнего бронирования
     */
    public void setLastBooking(BookingForItemDto lastBooking) {
        this.lastBooking = lastBooking;
    }

    /**
     * Возвращает информацию о следующем бронировании.
     *
     * @return DTO следующего бронирования
     */
    public BookingForItemDto getNextBooking() {
        return nextBooking;
    }

    /**
     * Устанавливает информацию о следующем бронировании.
     *
     * @param nextBooking DTO следующего бронирования
     */
    public void setNextBooking(BookingForItemDto nextBooking) {
        this.nextBooking = nextBooking;
    }

    /**
     * Преобразует DTO ответа в базовый DTO.
     *
     * @return базовый DTO вещи
     */
    public ItemBaseDto toBaseDto() {
        return new ItemBaseDto(name, description, available, requestId);
    }
}
package ru.practicum.shareit.item.dto;

/**
 * DTO для отображения информации о бронировании в контексте вещи
 */
public class BookingForItemDto {
    private Long id;
    private Long bookerId;

    /**
     * Конструктор по умолчанию.
     */
    public BookingForItemDto() {
    }

    /**
     * Конструктор с параметрами.
     *
     * @param id       идентификатор бронирования
     * @param bookerId идентификатор пользователя, который забронировал вещь
     */
    public BookingForItemDto(Long id, Long bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }

    /**
     * Возвращает идентификатор бронирования.
     *
     * @return идентификатор бронирования
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор бронирования.
     *
     * @param id идентификатор бронирования
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает идентификатор пользователя, который забронировал вещь.
     *
     * @return идентификатор пользователя
     */
    public Long getBookerId() {
        return bookerId;
    }

    /**
     * Устанавливает идентификатор пользователя, который забронировал вещь.
     *
     * @param bookerId идентификатор пользователя
     */
    public void setBookerId(Long bookerId) {
        this.bookerId = bookerId;
    }
}
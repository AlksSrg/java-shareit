package ru.practicum.shareit.exception.booking;

/**
 * Исключение, выбрасываемое когда бронирование не найдено.
 */
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
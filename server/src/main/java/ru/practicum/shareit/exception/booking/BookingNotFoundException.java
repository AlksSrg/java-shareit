package ru.practicum.shareit.exception.booking;

/**
 * Исключение, возникающее когда бронирование не найдено.
 */
public class BookingNotFoundException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public BookingNotFoundException(String message) {
        super(message);
    }
}
package ru.practicum.shareit.server.exception.booking;

/**
 * Исключение, возникающее когда пользователь пытается выполнить операцию
 * с бронированием, которое ему не принадлежит или не связано с его вещами.
 */
public class BookingNotOwnedException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public BookingNotOwnedException(String message) {
        super(message);
    }
}
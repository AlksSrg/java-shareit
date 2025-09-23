package ru.practicum.shareit.server.exception.booking;

/**
 * Исключение, возникающее когда пытаются изменить статус бронирования,
 * который уже был установлен ранее и не может быть изменен.
 */
public class BookingStatusAlreadySetException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public BookingStatusAlreadySetException(String message) {
        super(message);
    }
}
package ru.practicum.shareit.server.exception.booking;

/**
 * Исключение, возникающее когда вещь недоступна для бронирования.
 */
public class UnavailableItemException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public UnavailableItemException(String message) {
        super(message);
    }
}
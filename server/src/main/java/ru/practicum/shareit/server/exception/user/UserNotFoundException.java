package ru.practicum.shareit.server.exception.user;

/**
 * Исключение, возникающее когда пользователь не найден.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
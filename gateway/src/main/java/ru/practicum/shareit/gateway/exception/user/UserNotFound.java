package ru.practicum.shareit.gateway.exception.user;

/**
 * Исключение, возникающее когда пользователь не найден.
 */
public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }
}
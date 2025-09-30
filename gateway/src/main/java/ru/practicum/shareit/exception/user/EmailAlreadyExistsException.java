package ru.practicum.shareit.exception.user;

/**
 * Исключение, возникающее когда пользователь пытается использовать
 * уже существующий email при регистрации или обновлении данных.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
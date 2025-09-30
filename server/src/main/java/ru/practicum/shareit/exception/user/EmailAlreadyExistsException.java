package ru.practicum.shareit.exception.user;

/**
 * Исключение, выбрасываемое когда пользователь пытается использовать email,
 * который уже зарегистрирован в системе.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
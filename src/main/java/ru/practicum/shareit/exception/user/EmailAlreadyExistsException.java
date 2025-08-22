package ru.practicum.shareit.exception.user;

/**
 * Исключение, выбрасываемое при попытке создания пользователя с email,
 * который уже существует в системе.
 * Наследует {@link RuntimeException}, что делает его необрабатываемым исключением.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением об ошибке.
     *
     * @param message детальное сообщение об ошибке
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
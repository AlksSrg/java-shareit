package ru.practicum.shareit.server.exception.item;

/**
 * Исключение, выбрасываемое когда пользователь не является владельцем вещи.
 */
public class ItemNotOwnedByUserException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public ItemNotOwnedByUserException(String message) {
        super(message);
    }
}
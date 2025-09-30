package ru.practicum.shareit.exception.item;

/**
 * Исключение, выбрасываемое когда вещь не найдена.
 */
public class ItemNotFoundException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public ItemNotFoundException(String message) {
        super(message);
    }
}
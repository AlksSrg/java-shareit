package ru.practicum.shareit.exception.item;

/**
 * Исключение, выбрасываемое когда пользователь пытается выполнить операцию
 * с вещью, которая ему не принадлежит (например, обновление или удаление).
 * <p>
 * Наследует {@link RuntimeException}, что делает его необрабатываемым исключением.
 * Обычно возникает при нарушении правил доступа к данным.
 */
public class ItemNotOwnedByUserException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением об ошибке.
     *
     * @param message детальное сообщение об ошибке, содержащее идентификаторы
     *                пользователя и вещи
     */
    public ItemNotOwnedByUserException(String message) {
        super(message);
    }
}
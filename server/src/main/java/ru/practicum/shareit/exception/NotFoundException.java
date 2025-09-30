package ru.practicum.shareit.exception;

/**
 * Исключение, выбрасываемое когда сущность не найдена.
 * Базовое исключение для случаев, когда запрашиваемая сущность не существует.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public NotFoundException(String message) {
        super(message);
    }
}
package ru.practicum.shareit.gateway.exception.item;

/**
 * Исключение, выбрасываемое когда вещь недоступна для бронирования.
 */
public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}
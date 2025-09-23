package ru.practicum.shareit.exception.item;

/**
 * Исключение, возникающее когда вещь не найдена.
 */
public class ItemNotFound extends RuntimeException {
    public ItemNotFound(String message) {
        super(message);
    }
}
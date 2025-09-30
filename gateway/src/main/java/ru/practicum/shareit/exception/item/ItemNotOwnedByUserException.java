package ru.practicum.shareit.exception.item;

/**
 * Исключение, возникающее когда пользователь пытается выполнить операцию
 * с вещью, которая ему не принадлежит.
 */
public class ItemNotOwnedByUserException extends RuntimeException {
    public ItemNotOwnedByUserException(String message) {
        super(message);
    }
}
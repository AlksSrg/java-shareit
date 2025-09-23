package ru.practicum.shareit.gateway.exception;

/**
 * Исключение, возникающее когда запрашиваемый ресурс не найден.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
package ru.practicum.shareit.gateway.exception.item;

/**
 * Исключение, возникающее когда пользователь пытается оставить комментарий
 * к вещи, которую не бронировал или бронирование не завершено.
 */
public class CommentNotAllowedException extends RuntimeException {
    public CommentNotAllowedException(String message) {
        super(message);
    }
}
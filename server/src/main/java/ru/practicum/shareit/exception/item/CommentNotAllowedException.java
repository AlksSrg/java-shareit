package ru.practicum.shareit.exception.item;

/**
 * Исключение, выбрасываемое когда пользователь не может оставить комментарий.
 * Обычно возникает когда пользователь не бронировал вещь или бронирование не завершено.
 */
public class CommentNotAllowedException extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message детальное сообщение об ошибке
     */
    public CommentNotAllowedException(String message) {
        super(message);
    }
}
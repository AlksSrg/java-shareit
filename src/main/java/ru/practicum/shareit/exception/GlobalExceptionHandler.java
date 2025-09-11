package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.item.CommentNotAllowedException;
import ru.practicum.shareit.exception.item.ItemNotFound;
import ru.practicum.shareit.exception.item.ItemNotOwnedByUserException;
import ru.practicum.shareit.exception.user.UserNotFound;

/**
 * Глобальный обработчик исключений для REST контроллеров.
 * Перехватывает исключения, возникающие в процессе обработки HTTP запросов,
 * и возвращает структурированные JSON ответы с соответствующими HTTP статусами.
 * <p>
 * Использует аннотацию {@link RestControllerAdvice} для применения ко всем контроллерам.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения {@link UserNotFound}, возникающие когда пользователь не найден.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFound ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link ItemNotOwnedByUserException}, возникающие когда
     * пользователь пытается выполнить операцию с вещью, которая ему не принадлежит.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(ItemNotOwnedByUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotOwned(ItemNotOwnedByUserException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link ItemNotFound}, возникающие когда вещь не найдена.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(ItemNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFound(ItemNotFound ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает все неперехваченные исключения.
     * Является fallback-обработчиком для любых необработанных исключений.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с общим сообщением об ошибке
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse("Произошла внутренняя ошибка сервера");
    }

    /**
     * DTO класс для представления ошибок в JSON формате.
     * Содержит одно поле с сообщением об ошибке.
     *
     * @param error сообщение об ошибке
     */
    public record ErrorResponse(String error) {
    }

    /**
     * Обрабатывает исключения {@link CommentNotAllowedException}, возникающие когда
     * пользователь пытается оставить комментарий к вещи, которую не бронировал
     * или бронирование не завершено.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(CommentNotAllowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCommentNotAllowed(CommentNotAllowedException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link IllegalArgumentException}, возникающие когда
     * переданы невалидные аргументы в методы.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
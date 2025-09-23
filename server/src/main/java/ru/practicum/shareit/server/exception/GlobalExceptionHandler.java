package ru.practicum.shareit.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.server.exception.booking.BookingNotFoundException;
import ru.practicum.shareit.server.exception.booking.BookingNotOwnedException;
import ru.practicum.shareit.server.exception.booking.BookingStatusAlreadySetException;
import ru.practicum.shareit.server.exception.booking.UnavailableItemException;
import ru.practicum.shareit.server.exception.item.CommentNotAllowedException;
import ru.practicum.shareit.server.exception.item.ItemNotFoundException;
import ru.practicum.shareit.server.exception.item.ItemNotOwnedByUserException;
import ru.practicum.shareit.server.exception.user.EmailAlreadyExistsException;
import ru.practicum.shareit.server.exception.user.UserNotFoundException;

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
     * Обрабатывает исключения {@link UserNotFoundException}, возникающие когда пользователь не найден.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
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
     * Обрабатывает исключения {@link ItemNotFoundException}, возникающие когда вещь не найдена.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFoundException(ItemNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link BookingNotFoundException}, возникающие когда бронирование не найдено.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFound(BookingNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link UnavailableItemException}, возникающие когда вещь недоступна.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(UnavailableItemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemNotAvailable(UnavailableItemException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link BookingNotOwnedException}, возникающие когда
     * пользователь пытается выполнить операцию с бронированием, которое ему не принадлежит.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(BookingNotOwnedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleBookingNotOwned(BookingNotOwnedException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link BookingStatusAlreadySetException}, возникающие когда
     * пытаются изменить статус бронирования, который уже был установлен.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(BookingStatusAlreadySetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingStatusAlreadySet(BookingStatusAlreadySetException ex) {
        return new ErrorResponse(ex.getMessage());
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

    /**
     * Обрабатывает исключения {@link NotFoundException}, возникающие когда запрос не найден.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link EmailAlreadyExistsException}, возникающие когда
     * пользователь пытается использовать уже существующий email.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
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
     * Обрабатывает исключения {@link MissingRequestHeaderException}, возникающие когда
     * отсутствует обязательный заголовок запроса.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestHeader(MissingRequestHeaderException ex) {
        return new ErrorResponse("Отсутствует обязательный заголовок: " + ex.getHeaderName());
    }

    /**
     * DTO класс для представления ошибок в JSON формате.
     * Содержит одно поле с сообщением об ошибке.
     *
     * @param error сообщение об ошибке
     */
    public record ErrorResponse(String error) {
    }
}
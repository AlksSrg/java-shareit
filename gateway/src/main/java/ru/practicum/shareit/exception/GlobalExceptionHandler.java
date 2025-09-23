package ru.practicum.shareit.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.booking.BookingNotFoundException;
import ru.practicum.shareit.exception.item.CommentNotAllowedException;
import ru.practicum.shareit.exception.item.ItemNotAvailableException;
import ru.practicum.shareit.exception.item.ItemNotFound;
import ru.practicum.shareit.exception.item.ItemNotOwnedByUserException;
import ru.practicum.shareit.exception.user.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.user.UserNotFound;

import java.util.stream.Collectors;

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
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
    public ErrorResponse handleItemNotFound(ItemNotFound ex) {
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
    @ResponseBody
    public ErrorResponse handleBookingNotFound(BookingNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link ItemNotAvailableException}, возникающие когда вещь недоступна.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(ItemNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleItemNotAvailable(ItemNotAvailableException ex) {
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
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
    public ErrorResponse handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link ConstraintViolationException}, возникающие при валидации параметров.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        return new ErrorResponse("Неверные параметры запроса: " + ex.getMessage());
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
    @ResponseBody
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link MethodArgumentNotValidException}, возникающие при валидации DTO.
     *
     * @param ex перехваченное исключение
     * @return структурированный ответ с сообщением об ошибке валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new ErrorResponse("Невалидные данные: " + errorMessage);
    }

    /**
     * Обрабатывает исключение MissingRequestHeaderException, которое возникает при отсутствии
     * требуемого заголовка в HTTP-запросе.
     *
     * @param ex перехватываемое исключение типа MissingRequestHeaderException
     * @return объект ResponseEntity с HTTP статусом BAD_REQUEST и описанием ошибки
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingHeaderException(MissingRequestHeaderException ex) {
        String errorMessage = "Требуемый заголовок '" + ex.getHeaderName() + "' не найден";
        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
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
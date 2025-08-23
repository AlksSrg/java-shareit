package ru.practicum.shareit.exception.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое когда запрашиваемая вещь не найдена в системе.
 * Автоматически возвращает HTTP статус 404 (NOT_FOUND) при возникновении.
 * <p>
 * Наследует {@link RuntimeException}, что делает его необрабатываемым исключением.
 * Аннотация {@link ResponseStatus} устанавливает HTTP статус ответа.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFound extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением об ошибке.
     *
     * @param message детальное сообщение об ошибке, содержащее идентификатор вещи
     */
    public ItemNotFound(String message) {
        super(message);
    }
}
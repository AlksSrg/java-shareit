package ru.practicum.shareit.exception.user;

/**
 * Исключение, выбрасываемое когда запрашиваемый пользователь не найден в системе.
 * <p>
 * Наследует {@link RuntimeException}, что делает его необрабатываемым исключением.
 * Возникает при операциях с несуществующим идентификатором пользователя.
 */
public class UserNotFound extends RuntimeException {

    /**
     * Создает новое исключение с указанным сообщением об ошибке.
     *
     * @param message детальное сообщение об ошибке, содержащее идентификатор пользователя
     */
    public UserNotFound(String message) {
        super(message);
    }
}
package ru.practicum.shareit.user.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

/**
 * Клиент для взаимодействия с сервером пользователей.
 * Обеспечивает REST вызовы к основному серверу для операций с пользователями.
 */
@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    /**
     * Конструктор клиента пользователей.
     *
     * @param restTemplate REST template для HTTP запросов
     */
    public UserClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    /**
     * Создает нового пользователя на сервере.
     *
     * @param request данные для создания пользователя
     * @return ответ сервера с созданным пользователем
     */
    public ResponseEntity<Object> create(UserCreateRequestDto request) {
        return post(API_PREFIX, null, request);
    }

    /**
     * Обновляет существующего пользователя на сервере.
     *
     * @param userId  идентификатор пользователя
     * @param request данные для обновления пользователя
     * @return ответ сервера с обновленным пользователем
     */
    public ResponseEntity<Object> update(Long userId, UserUpdateRequestDto request) {
        return patch(API_PREFIX + "/" + userId, null, null, request);
    }

    /**
     * Удаляет пользователя на сервере.
     *
     * @param userId идентификатор пользователя
     * @return ответ сервера со статусом операции
     */
    public ResponseEntity<Object> delete(Long userId) {
        return delete(API_PREFIX + "/" + userId, null);
    }

    /**
     * Находит пользователя по идентификатору на сервере.
     *
     * @param userId идентификатор пользователя
     * @return ответ сервера с информацией о пользователе
     */
    public ResponseEntity<Object> findById(Long userId) {
        return get(API_PREFIX + "/" + userId, null);
    }

    /**
     * Находит всех пользователей на сервере.
     *
     * @return ответ сервера со списком всех пользователей
     */
    public ResponseEntity<Object> findAll() {
        return get(API_PREFIX, null);
    }
}
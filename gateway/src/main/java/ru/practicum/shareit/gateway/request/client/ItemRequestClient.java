package ru.practicum.shareit.gateway.request.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.request.dto.ItemRequestDto;

import java.util.Map;

/**
 * Клиент для взаимодействия с сервером запросов вещей.
 * Обеспечивает REST вызовы к основному серверу для операций с запросами.
 * Наследует базовый функционал от {@link BaseClient}.
 */
@Component
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    /**
     * Конструктор клиента запросов вещей.
     *
     * @param restTemplate REST template для HTTP запросов
     */
    public ItemRequestClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    /**
     * Создает новый запрос вещи на сервере.
     *
     * @param request DTO запроса вещи
     * @param userId  идентификатор пользователя
     * @return ответ сервера с созданным запросом
     */
    public ResponseEntity<Object> create(ItemRequestDto request, Long userId) {
        return post(API_PREFIX, userId, request);
    }

    /**
     * Получает собственные запросы пользователя на сервере.
     *
     * @param userId идентификатор пользователя
     * @return ответ сервера со списком запросов пользователя
     */
    public ResponseEntity<Object> getOwnRequests(Long userId) {
        return get(API_PREFIX, userId);
    }

    /**
     * Получает все запросы других пользователей на сервере с пагинацией.
     *
     * @param userId     идентификатор пользователя
     * @param parameters параметры пагинации
     * @return ответ сервера со списком запросов других пользователей
     */
    public ResponseEntity<Object> getAllRequests(Long userId, Map<String, Object> parameters) {
        return get(API_PREFIX + "/all?from={from}&size={size}", userId, parameters);
    }

    /**
     * Получает запрос по идентификатору на сервере.
     *
     * @param requestId идентификатор запроса
     * @param userId    идентификатор пользователя
     * @return ответ сервера с найденным запросом
     */
    public ResponseEntity<Object> getById(Long requestId, Long userId) {
        return get(API_PREFIX + "/" + requestId, userId);
    }
}
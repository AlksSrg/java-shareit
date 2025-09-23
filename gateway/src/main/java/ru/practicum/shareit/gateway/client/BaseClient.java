package ru.practicum.shareit.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Базовый клиент для выполнения HTTP запросов к серверу.
 * Предоставляет общие методы для работы с REST API.
 * Наследуется специализированными клиентами для конкретных сущностей.
 */
public abstract class BaseClient {
    protected final RestTemplate rest;

    @Value("${server.url:http://localhost:9090}")
    private String serverUrl;

    /**
     * Конструктор базового клиента.
     *
     * @param rest REST template для выполнения HTTP запросов
     */
    protected BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    /**
     * Выполняет POST запрос.
     *
     * @param path   путь к endpoint
     * @param userId идентификатор пользователя
     * @param body   тело запроса
     * @return ответ сервера
     */
    protected ResponseEntity<Object> post(String path, Long userId, Object body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, body);
    }

    /**
     * Выполняет GET запрос без параметров.
     *
     * @param path   путь к endpoint
     * @param userId идентификатор пользователя
     * @return ответ сервера
     */
    protected ResponseEntity<Object> get(String path, Long userId) {
        return get(path, userId, null);
    }

    /**
     * Выполняет GET запрос с параметрами.
     *
     * @param path       путь к endpoint
     * @param userId     идентификатор пользователя
     * @param parameters параметры запроса
     * @return ответ сервера
     */
    protected ResponseEntity<Object> get(String path, Long userId, Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, null, parameters);
    }

    /**
     * Выполняет PATCH запрос.
     *
     * @param path       путь к endpoint
     * @param userId     идентификатор пользователя
     * @param parameters параметры запроса
     * @param body       тело запроса
     * @return ответ сервера
     */
    protected ResponseEntity<Object> patch(String path, Long userId, Map<String, Object> parameters, Object body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, body, parameters);
    }

    /**
     * Выполняет DELETE запрос.
     *
     * @param path   путь к endpoint
     * @param userId идентификатор пользователя
     * @return ответ сервера
     */
    protected ResponseEntity<Object> delete(String path, Long userId) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, null);
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId, Object body) {
        return makeAndSendRequest(method, path, userId, body, null);
    }

    /**
     * Создает и отправляет HTTP запрос.
     *
     * @param method     HTTP метод
     * @param path       путь к endpoint
     * @param userId     идентификатор пользователя
     * @param body       тело запроса
     * @param parameters параметры запроса
     * @return ответ сервера
     */
    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path,
                                                      Long userId, Object body,
                                                      Map<String, Object> parameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (userId != null) {
            headers.set("X-Sharer-User-Id", userId.toString());
        }

        HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
        String url = serverUrl + path;

        try {
            if (parameters != null && !parameters.isEmpty()) {
                return rest.exchange(url, method, requestEntity, Object.class, parameters);
            } else {
                return rest.exchange(url, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }
}
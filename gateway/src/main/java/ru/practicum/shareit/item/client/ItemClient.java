package ru.practicum.shareit.item.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

import java.util.Map;

/**
 * Клиент для взаимодействия с сервером вещей.
 * Обеспечивает REST вызовы к основному серверу для операций с вещами.
 * Наследует базовый функционал от {@link BaseClient}.
 */
@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    /**
     * Конструктор клиента вещей.
     *
     * @param restTemplate REST template для HTTP запросов
     */
    public ItemClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    /**
     * Создает новую вещь на сервере.
     *
     * @param ownerId идентификатор владельца
     * @param request данные для создания вещи
     * @return ответ сервера с созданной вещью
     */
    public ResponseEntity<Object> create(Long ownerId, ItemCreateRequestDto request) {
        return post(API_PREFIX, ownerId, request);
    }

    /**
     * Обновляет существующую вещь на сервере.
     *
     * @param itemId  идентификатор вещи
     * @param ownerId идентификатор владельца
     * @param request данные для обновления вещи
     * @return ответ сервера с обновленной вещью
     */
    public ResponseEntity<Object> update(Long itemId, Long ownerId, ItemUpdateRequestDto request) {
        return patch(API_PREFIX + "/" + itemId, ownerId, null, request);
    }

    /**
     * Удаляет вещь на сервере.
     *
     * @param itemId  идентификатор вещи
     * @param ownerId идентификатор владельца
     * @return ответ сервера со статусом операции
     */
    public ResponseEntity<Object> delete(Long itemId, Long ownerId) {
        return delete(API_PREFIX + "/" + itemId, ownerId);
    }

    /**
     * Находит вещь по идентификатору на сервере.
     *
     * @param itemId идентификатор вещи
     * @return ответ сервера с найденной вещью
     */
    public ResponseEntity<Object> findById(Long itemId) {
        return get(API_PREFIX + "/" + itemId, null);
    }

    /**
     * Находит вещи владельца на сервере с пагинацией.
     *
     * @param ownerId идентификатор владельца
     * @param from    начальный индекс для пагинации
     * @param size    количество элементов на странице
     * @return ответ сервера со списком вещей владельца
     */
    public ResponseEntity<Object> findByOwnerId(Long ownerId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "?from={from}&size={size}", ownerId, parameters);
    }

    /**
     * Ищет доступные вещи по тексту на сервере.
     *
     * @param text текст для поиска в названии и описании
     * @param from начальный индекс для пагинации
     * @param size количество элементов на странице
     * @return ответ сервера со списком найденных вещей
     */
    public ResponseEntity<Object> searchAvailableItems(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/search?text={text}&from={from}&size={size}", null, parameters);
    }

    /**
     * Добавляет комментарий к вещи на сервере.
     *
     * @param itemId  идентификатор вещи
     * @param userId  идентификатор пользователя
     * @param request данные для создания комментария
     * @return ответ сервера с созданным комментарием
     */
    public ResponseEntity<Object> addComment(Long itemId, Long userId, CommentCreateRequestDto request) {
        return post(API_PREFIX + "/" + itemId + "/comment", userId, request);
    }
}
package ru.practicum.shareit.booking.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

/**
 * Клиент для взаимодействия с сервером бронирований.
 * Обеспечивает REST вызовы к основному серверу для операций с бронированиями.
 * Наследует базовый функционал от {@link BaseClient}.
 */
@Component
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    /**
     * Конструктор клиента бронирований.
     *
     * @param restTemplate REST template для HTTP запросов
     */
    public BookingClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    /**
     * Создает новое бронирование на сервере.
     *
     * @param userId  идентификатор пользователя
     * @param request данные для создания бронирования
     * @return ответ сервера с созданным бронированием
     */
    public ResponseEntity<Object> create(Long userId, BookingCreateRequestDto request) {
        return post(API_PREFIX, userId, request);
    }

    /**
     * Обновляет статус бронирования на сервере.
     *
     * @param userId    идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @param approved  флаг подтверждения (true - подтверждено, false - отклонено)
     * @return ответ сервера с обновленным бронированием
     */
    public ResponseEntity<Object> updateStatus(Long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch(API_PREFIX + "/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    /**
     * Находит бронирование по идентификатору на сервере.
     *
     * @param userId    идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @return ответ сервера с найденным бронированием
     */
    public ResponseEntity<Object> findById(Long userId, Long bookingId) {
        return get(API_PREFIX + "/" + bookingId, userId);
    }

    /**
     * Находит бронирования пользователя на сервере с фильтрацией по статусу.
     *
     * @param bookerId идентификатор пользователя-арендатора
     * @param status   статус бронирования для фильтрации
     * @param from     начальный индекс для пагинации
     * @param size     количество элементов на странице
     * @return ответ сервера со списком бронирований пользователя
     */
    public ResponseEntity<Object> findByBookerId(Long bookerId, String status, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", status,
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "?state={state}&from={from}&size={size}", bookerId, parameters);
    }

    /**
     * Находит бронирования владельца на сервере с фильтрацией по статусу.
     *
     * @param ownerId идентификатор владельца вещей
     * @param status  статус бронирования для фильтрации
     * @param from    начальный индекс для пагинации
     * @param size    количество элементов на странице
     * @return ответ сервера со списком бронирований для вещей владельца
     */
    public ResponseEntity<Object> findByOwnerId(Long ownerId, String status, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", status,
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/owner?state={state}&from={from}&size={size}", ownerId, parameters);
    }
}
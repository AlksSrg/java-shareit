package ru.practicum.shareit.gateway.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.request.client.ItemRequestClient;
import ru.practicum.shareit.gateway.request.dto.ItemRequestDto;

import java.util.Map;

/**
 * Контроллер для работы с запросами вещей на gateway.
 * Обеспечивает REST API для управления информацией о запросах вещей.
 * Выполняет валидацию входных данных и проксирует запросы на основной сервер.
 */
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    /**
     * Добавляет новый запрос вещи.
     *
     * @param itemRequestDto DTO запроса вещи
     * @param userId         идентификатор пользователя
     * @return созданный запрос вещи
     */
    @PostMapping
    public ResponseEntity<Object> createItemRequest(
            @Valid @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.create(itemRequestDto, userId);
    }

    /**
     * Получает список своих запросов вместе с данными об ответах на них.
     *
     * @param userId идентификатор пользователя
     * @return список запросов пользователя
     */
    @GetMapping
    public ResponseEntity<Object> getOwnItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getOwnRequests(userId);
    }

    /**
     * Получает список запросов, созданных другими пользователями.
     *
     * @param userId идентификатор пользователя
     * @param from   индекс первого элемента
     * @param size   количество элементов для отображения
     * @return список запросов других пользователей
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return itemRequestClient.getAllRequests(userId, parameters);
    }

    /**
     * Получает данные об одном конкретном запросе вместе с данными об ответах на него.
     *
     * @param requestId идентификатор запроса
     * @param userId    идентификатор пользователя
     * @return запрос вещи
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @PathVariable Long requestId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getById(requestId, userId);
    }
}
package ru.practicum.shareit.server.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.service.RequestService;

import java.util.List;

/**
 * Контроллер для работы с запросами вещей на сервере.
 * Обеспечивает REST API для управления информацией о запросах вещей.
 * Выполняет бизнес-логику без валидации входных данных.
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestService requestService;

    /**
     * Добавляет новый запрос вещи.
     *
     * @param itemRequestDto DTO запроса вещи
     * @param userId         идентификатор пользователя
     * @return созданный запрос вещи
     */
    @PostMapping
    public ItemRequestDto createItemRequest(
            @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Создание запроса вещи для пользователя {}", userId);
        return requestService.createItemRequest(itemRequestDto, userId);
    }

    /**
     * Получает список своих запросов вместе с данными об ответах на них.
     *
     * @param userId идентификатор пользователя
     * @return список запросов пользователя
     */
    @GetMapping
    public List<ItemRequestDto> getOwnItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение собственных запросов пользователя {}", userId);
        return requestService.getOwnItemRequests(userId);
    }

    /**
     * Получает список запросов, созданных другими пользователями.
     *
     * @param userId идентификатор пользователя
     * @param from   начальная позиция для пагинации
     * @param size   количество элементов для отображения
     * @return список запросов других пользователей
     */
    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение запросов других пользователей для userId={}, from={}, size={}", userId, from, size);
        return requestService.getAllItemRequests(userId, from, size);
    }

    /**
     * Получает данные об одном конкретном запросе вместе с данными об ответах на него.
     *
     * @param requestId идентификатор запроса
     * @param userId    идентификатор пользователя
     * @return запрос вещи
     */
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(
            @PathVariable Long requestId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение запроса по ID {} для пользователя {}", requestId, userId);
        return requestService.getItemRequestById(requestId, userId);
    }
}
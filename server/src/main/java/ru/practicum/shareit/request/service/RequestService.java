package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * Сервис для работы с запросами вещей.
 * Предоставляет методы для создания и получения запросов вещей.
 */
public interface RequestService {

    /**
     * Создает новый запрос вещи.
     *
     * @param itemRequestDto DTO запроса вещи
     * @param userId         идентификатор пользователя
     * @return созданный запрос вещи
     */
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId);

    /**
     * Получает список запросов пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список запросов пользователя
     */
    List<ItemRequestDto> getOwnItemRequests(Long userId);

    /**
     * Получает список запросов других пользователей.
     *
     * @param userId идентификатор пользователя
     * @param from   начальная позиция для пагинации
     * @param size   количество элементов для отображения
     * @return список запросов других пользователей
     */
    List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size);

    /**
     * Получает запрос вещи по идентификатору.
     *
     * @param requestId идентификатор запроса
     * @param userId    идентификатор пользователя
     * @return запрос вещи
     */
    ItemRequestDto getItemRequestById(Long requestId, Long userId);
}
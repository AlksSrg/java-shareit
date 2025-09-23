package ru.practicum.shareit.server.item.service;

import ru.practicum.shareit.server.item.dto.*;
import ru.practicum.shareit.server.item.model.Item;

import java.util.List;

/**
 * Сервис для работы с вещами.
 * Предоставляет бизнес-логику для операций с вещами.
 */
public interface ItemService {

    /**
     * Создает новую вещь.
     *
     * @param ownerId              идентификатор владельца вещи
     * @param itemCreateRequestDto DTO с данными для создания вещи
     * @return созданная вещь в формате DTO
     */
    ItemResponseDto create(Long ownerId, ItemCreateRequestDto itemCreateRequestDto);

    /**
     * Обновляет существующую вещь.
     *
     * @param id                   идентификатор вещи
     * @param ownerId              идентификатор владельца вещи
     * @param itemUpdateRequestDto DTO с данными для обновления
     * @return обновленная вещь в формате DTO
     */
    ItemResponseDto update(Long id, Long ownerId, ItemUpdateRequestDto itemUpdateRequestDto);

    /**
     * Удаляет вещь по идентификатору.
     *
     * @param id      идентификатор вещи
     * @param ownerId идентификатор владельца вещи
     */
    void delete(Long id, Long ownerId);

    /**
     * Находит вещь по идентификатору.
     *
     * @param id идентификатор вещи
     * @return вещь в формате DTO
     */
    ItemResponseDto findById(Long id);

    /**
     * Находит вещь по идентификатору или выбрасывает исключение если не найдена.
     *
     * @param id идентификатор вещи
     * @return сущность вещи
     */
    Item getItemOrThrow(Long id);

    /**
     * Находит все вещи владельца с пагинацией.
     *
     * @param ownerId идентификатор владельца
     * @param from    начальная позиция
     * @param size    количество элементов
     * @return список вещей владельца
     */
    List<ItemResponseDto> findByOwnerId(Long ownerId, Integer from, Integer size);

    /**
     * Находит вещи по идентификатору запроса.
     *
     * @param requestId идентификатор запроса
     * @return список вещей созданных по запросу
     */
    List<ItemResponseDto> findByRequestId(Long requestId);

    /**
     * Ищет доступные вещи по тексту в названии или описании.
     *
     * @param text текст для поиска
     * @param from начальная позиция для пагинации
     * @param size количество элементов на странице
     * @return список найденных вещей
     */
    List<ItemResponseDto> searchAvailableItems(String text, Integer from, Integer size);

    /**
     * Добавляет комментарий к вещи.
     *
     * @param itemId                  идентификатор вещи
     * @param userId                  идентификатор пользователя
     * @param commentCreateRequestDto DTO с данными для создания комментария
     * @return созданный комментарий в формате DTO
     */
    CommentResponseDto addComment(Long itemId, Long userId, CommentCreateRequestDto commentCreateRequestDto);
}
package ru.practicum.shareit.server.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.item.dto.*;
import ru.practicum.shareit.server.item.service.ItemService;

import java.util.List;

/**
 * Контроллер для работы с вещами на сервере.
 * Обеспечивает REST API для управления информацией о вещах.
 * Выполняет бизнес-логику без валидации входных данных.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    /**
     * Создает новую вещь.
     *
     * @param ownerId              идентификатор владельца вещи
     * @param itemCreateRequestDto DTO с данными для создания вещи
     * @return созданная вещь в формате DTO
     */
    @PostMapping
    public ItemResponseDto createItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @RequestBody ItemCreateRequestDto itemCreateRequestDto) {
        return itemService.create(ownerId, itemCreateRequestDto);
    }

    /**
     * Обновляет существующую вещь.
     *
     * @param id                   идентификатор вещи
     * @param ownerId              идентификатор владельца вещи
     * @param itemUpdateRequestDto DTO с данными для обновления вещи
     * @return обновленная вещь в формате DTO
     */
    @PatchMapping("/{id}")
    public ItemResponseDto updateItem(@PathVariable Long id,
                                      @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @RequestBody ItemUpdateRequestDto itemUpdateRequestDto) {
        return itemService.update(id, ownerId, itemUpdateRequestDto);
    }

    /**
     * Удаляет вещь по идентификатору.
     *
     * @param id      идентификатор вещи
     * @param ownerId идентификатор владельца вещи
     */
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id,
                           @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        itemService.delete(id, ownerId);
    }

    /**
     * Получает вещь по идентификатору.
     *
     * @param id идентификатор вещи
     * @return вещь в формате DTO
     */
    @GetMapping("/{id}")
    public ItemResponseDto getItem(@PathVariable Long id) {
        return itemService.findById(id);
    }

    /**
     * Получает список вещей пользователя с пагинацией.
     *
     * @param ownerId идентификатор владельца вещей
     * @param from    начальная позиция для пагинации
     * @param size    количество элементов на странице
     * @return список вещей пользователя
     */
    @GetMapping
    public List<ItemResponseDto> getUsersItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        return itemService.findByOwnerId(ownerId, from, size);
    }

    /**
     * Ищет доступные вещи по тексту.
     *
     * @param text текст для поиска в названии и описании
     * @param from начальная позиция для пагинации
     * @param size количество элементов на странице
     * @return список найденных вещей
     */
    @GetMapping("/search")
    public List<ItemResponseDto> searchItems(@RequestParam String text,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return itemService.searchAvailableItems(text, from, size);
    }

    /**
     * Добавляет комментарий к вещи.
     *
     * @param itemId                  идентификатор вещи
     * @param userId                  идентификатор пользователя
     * @param commentCreateRequestDto DTO с данными для создания комментария
     * @return созданный комментарий в формате DTO
     */
    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@PathVariable Long itemId,
                                         @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        return itemService.addComment(itemId, userId, commentCreateRequestDto);
    }
}
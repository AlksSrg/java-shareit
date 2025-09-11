package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * Контроллер для работы с вещами.
 * Обеспечивает REST API для управления информацией о вещах.
 * Поддерживает операции:
 * - CRUD операции с вещами
 * Все методы работают с сущностью {@link Item} и используют {@link ItemService} для бизнес-логики.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    /**
     * Создание новой вещи.
     *
     * @param ownerId              идентификатор владельца вещи
     * @param itemCreateRequestDto DTO с данными для создания вещи
     * @return созданная вещь в формате DTO
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemResponseDto createItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @Valid @RequestBody ItemCreateRequestDto itemCreateRequestDto) {
        return itemService.create(ownerId, itemCreateRequestDto);
    }

    /**
     * Обновление существующей вещи.
     *
     * @param id                   идентификатор вещи
     * @param ownerId              идентификатор владельца вещи
     * @param itemUpdateRequestDto DTO с данными для обновления вещи
     * @return обновленная вещь в формате DTO
     */
    @PatchMapping("/{id}")
    public ItemResponseDto updateItem(@PathVariable Long id,
                                      @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @Valid @RequestBody ItemUpdateRequestDto itemUpdateRequestDto) {
        return itemService.update(id, ownerId, itemUpdateRequestDto);
    }

    /**
     * Удаление вещи по идентификатору.
     *
     * @param id      идентификатор вещи
     * @param ownerId идентификатор владельца вещи
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id,
                           @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        itemService.delete(id, ownerId);
    }

    /**
     * Получение вещи по идентификатору.
     *
     * @param id идентификатор вещи
     * @return вещь в формате DTO
     */
    @GetMapping("/{id}")
    public ItemResponseDto getItem(@PathVariable Long id) {
        return itemService.findById(id);
    }

    /**
     * Получение списка вещей пользователя с пагинацией.
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
     * Поиск доступных вещей по тексту.
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
        // Проверяем, что текст не пустой
        if (text == null || text.trim().isEmpty()) {
            return List.of();
        }
        return itemService.searchAvailableItems(text, from, size);
    }

    /**
     * Добавляет комментарий к вещи.
     *
     * @param itemId                   идентификатор вещи
     * @param userId                   идентификатор пользователя
     * @param commentCreateRequestDto DTO с данными для создания комментария
     * @return созданный комментарий в формате DTO
     */
    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@PathVariable Long itemId,
                                         @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        return itemService.addComment(itemId, userId, commentCreateRequestDto);
    }
}
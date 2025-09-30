package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

import java.util.Collections;

/**
 * Контроллер для работы с вещами на gateway.
 * Обеспечивает REST API для управления информацией о вещах.
 * Выполняет валидацию входных данных и проксирует запросы на основной сервер.
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    /**
     * Создает новую вещь.
     *
     * @param ownerId              идентификатор владельца вещи
     * @param itemCreateRequestDto DTO с данными для создания вещи
     * @return созданная вещь
     */
    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @Valid @RequestBody ItemCreateRequestDto itemCreateRequestDto) {
        return itemClient.create(ownerId, itemCreateRequestDto);
    }

    /**
     * Обновляет существующую вещь.
     *
     * @param id                   идентификатор вещи
     * @param ownerId              идентификатор владельца вещи
     * @param itemUpdateRequestDto DTO с данными для обновления вещи
     * @return обновленная вещь
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable Long id,
                                             @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @Valid @RequestBody ItemUpdateRequestDto itemUpdateRequestDto) {
        return itemClient.update(id, ownerId, itemUpdateRequestDto);
    }

    /**
     * Удаляет вещь по идентификатору.
     *
     * @param id      идентификатор вещи
     * @param ownerId идентификатор владельца вещи
     * @return статус операции
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long id,
                                             @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.delete(id, ownerId);
    }

    /**
     * Получает вещь по идентификатору.
     *
     * @param id идентификатор вещи
     * @return информация о вещи
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable Long id) {
        return itemClient.findById(id);
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
    public ResponseEntity<Object> getUsersItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        return itemClient.findByOwnerId(ownerId, from, size);
    }

    /**
     * Ищет доступные вещи по тексту.
     * Если текст пустой или состоит только из пробелов, возвращается пустой список.
     *
     * @param text текст для поиска в названии и описании
     * @param from начальная позиция для пагинации
     * @param size количество элементов на странице
     * @return список найденных вещей (пустой список если текст для поиска отсутствует)
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.searchAvailableItems(text, from, size);
    }

    /**
     * Добавляет комментарий к вещи.
     *
     * @param itemId                  идентификатор вещи
     * @param userId                  идентификатор пользователя
     * @param commentCreateRequestDto DTO с данными для создания комментария
     * @return созданный комментарий
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        return itemClient.addComment(itemId, userId, commentCreateRequestDto);
    }
}
package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;

/**
 * Контроллер для работы с бронированием вещей.
 * Обеспечивает REST API для управления информацией о бронировании вещей.
 * Выполняет валидацию входных данных и проксирует запросы на основной сервер.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingClient bookingClient;

    /**
     * Создает новое бронирование.
     *
     * @param userId                  идентификатор пользователя создающего бронирование
     * @param bookingCreateRequestDto данные для создания бронирования
     * @return созданное бронирование
     */
    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody BookingCreateRequestDto bookingCreateRequestDto) {
        return bookingClient.create(userId, bookingCreateRequestDto);
    }

    /**
     * Обновляет статус бронирования (подтверждение/отклонение).
     *
     * @param userId    идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @param approved  флаг подтверждения (true - подтверждено, false - отклонено)
     * @return обновленное бронирование
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        return bookingClient.updateStatus(userId, bookingId, approved);
    }

    /**
     * Находит бронирование по идентификатору.
     *
     * @param userId    идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @return найденное бронирование
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        return bookingClient.findById(userId, bookingId);
    }

    /**
     * Находит все бронирования пользователя с фильтрацией по статусу.
     *
     * @param bookerId идентификатор пользователя-арендатора
     * @param status   статус бронирования для фильтрации
     * @param from     начальный индекс для пагинации
     * @param size     количество элементов на странице
     * @return список бронирований пользователя
     */
    @GetMapping
    public ResponseEntity<Object> findByBookerId(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return bookingClient.findByBookerId(bookerId, status, from, size);
    }

    /**
     * Находит все бронирования для вещей владельца с фильтрацией по статусу.
     *
     * @param ownerId идентификатор владельца вещей
     * @param status  статус бронирования для фильтрации
     * @param from    начальный индекс для пагинации
     * @param size    количество элементов на странице
     * @return список бронирований для вещей владельца
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> findByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return bookingClient.findByOwnerId(ownerId, status, from, size);
    }
}
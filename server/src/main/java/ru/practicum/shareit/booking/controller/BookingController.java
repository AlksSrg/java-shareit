package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * Контроллер для работы с бронированием вещей на сервере.
 * Обеспечивает REST API для управления информацией о бронировании вещей.
 * Выполняет бизнес-логику без валидации входных данных.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Создает новое бронирование.
     *
     * @param userId                  идентификатор пользователя создающего бронирование
     * @param bookingCreateRequestDto данные для создания бронирования
     * @return созданное бронирование
     */
    @PostMapping
    public BookingResponseDto create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody BookingCreateRequestDto bookingCreateRequestDto) {
        return bookingService.create(userId, bookingCreateRequestDto);
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
    public BookingResponseDto updateStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    /**
     * Находит бронирование по идентификатору.
     *
     * @param userId    идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @return найденное бронирование
     */
    @GetMapping("/{bookingId}")
    public BookingResponseDto findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        return bookingService.findById(userId, bookingId);
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
    public List<BookingResponseDto> findByBookerId(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam(defaultValue = "ALL") BookingStatus status,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.findByBookerId(bookerId, status, from, size);
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
    public List<BookingResponseDto> findByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(defaultValue = "ALL") BookingStatus status,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.findByOwnerId(ownerId, status, from, size);
    }
}
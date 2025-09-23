package ru.practicum.shareit.server.booking.service;

import ru.practicum.shareit.server.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.server.booking.dto.BookingResponseDto;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.BookingStatus;

import java.util.List;

/**
 * Сервис для работы с бронированиями.
 * Предоставляет методы для создания, обновления и поиска бронирований.
 */
public interface BookingService {

    /**
     * Создает новое бронирование.
     *
     * @param userId                  идентификатор пользователя
     * @param bookingCreateRequestDto данные для создания бронирования
     * @return созданное бронирование
     */
    BookingResponseDto create(Long userId, BookingCreateRequestDto bookingCreateRequestDto);

    /**
     * Обновляет статус бронирования (подтверждение/отклонение).
     *
     * @param userId    идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @param approved  флаг подтверждения
     * @return обновленное бронирование
     */
    BookingResponseDto updateStatus(Long userId, Long bookingId, Boolean approved);

    /**
     * Находит бронирование по идентификатору.
     *
     * @param userId    идентификатор пользователя
     * @param bookingId идентификатор бронирования
     * @return найденное бронирование
     */
    BookingResponseDto findById(Long userId, Long bookingId);

    /**
     * Находит все бронирования пользователя с фильтрацией по статусу.
     *
     * @param bookerId идентификатор пользователя-арендатора
     * @param status   состояние бронирования для фильтрации
     * @param from     начальный индекс для пагинации
     * @param size     количество элементов на странице
     * @return список бронирований пользователя
     */
    List<BookingResponseDto> findByBookerId(Long bookerId, BookingStatus status, Integer from, Integer size);

    /**
     * Находит все бронирования для вещей владельца с фильтрацией по статусу.
     *
     * @param ownerId идентификатор владельца вещей
     * @param status  состояние бронирования для фильтрации
     * @param from    начальный индекс для пагинации
     * @param size    количество элементов на странице
     * @return список бронирований для вещей владельца
     */
    List<BookingResponseDto> findByOwnerId(Long ownerId, BookingStatus status, Integer from, Integer size);

    /**
     * Получает бронирование по идентификатору или выбрасывает исключение если не найдено.
     *
     * @param bookingId идентификатор бронирования
     * @return найденное бронирование
     */
    Booking getBookingOrThrow(Long bookingId);
}
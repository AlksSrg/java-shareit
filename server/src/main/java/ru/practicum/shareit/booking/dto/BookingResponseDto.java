package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о бронировании.
 * Содержит полную информацию о бронировании, включая данные о вещи и пользователе.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {

    /**
     * Уникальный идентификатор бронирования
     */
    private Long id;

    /**
     * Дата и время начала бронирования
     */
    private LocalDateTime start;

    /**
     * Дата и время окончания бронирования
     */
    private LocalDateTime end;

    /**
     * Статус бронирования
     */
    private BookingStatus status;

    /**
     * Информация о забронированной вещи
     */
    private ItemResponseDto item;

    /**
     * Информация о пользователе, который осуществил бронирование
     */
    private UserResponseDto booker;
}
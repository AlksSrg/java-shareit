package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO для создания нового бронирования.
 * Содержит валидационные аннотации для обязательных полей.
 * Используется для передачи данных от клиента к серверу при создании бронирования.
 */
public record BookingCreateRequestDto(
        @NotNull(message = "Идентификатор вещи не может быть пустым")
        Long itemId,

        @NotNull(message = "Дата начала бронирования не может быть пустой")
        LocalDateTime start,

        @NotNull(message = "Дата окончания бронирования не может быть пустой")
        @Future(message = "Дата окончания бронирования должна быть в будущем")
        LocalDateTime end
) {
}
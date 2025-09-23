package ru.practicum.shareit.gateway.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
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
        @FutureOrPresent(message = "Дата начала бронирования должна быть в настоящем или будущем")
        LocalDateTime start,

        @NotNull(message = "Дата окончания бронирования не может быть пустой")
        @Future(message = "Дата окончания бронирования должна быть в будущем")
        LocalDateTime end
) {
}
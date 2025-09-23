package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingCreateRequestDto(
        Long itemId,

        @NotNull(message = "Дата начала бронирования не может быть пустой")
        @FutureOrPresent(message = "Дата начала бронирования должна быть в настоящем или будущем")
        LocalDateTime start,

        @NotNull(message = "Дата окончания бронирования не может быть пустой")
        @Future(message = "Дата окончания бронирования должна быть в будущем")
        LocalDateTime end
) {
}
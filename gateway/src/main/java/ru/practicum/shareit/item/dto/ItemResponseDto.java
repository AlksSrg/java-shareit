package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для возврата информации о вещи
 */
public record ItemResponseDto(
        Long id,
        String name,
        String description,
        Boolean available,
        Long requestId,

        @JsonProperty("lastBooking")
        BookingShortDto lastBooking,

        @JsonProperty("nextBooking")
        BookingShortDto nextBooking,

        List<CommentDto> comments
) {
    public record BookingShortDto(Long id, Long bookerId) {
    }

    public record CommentDto(
            Long id,
            String text,
            String authorName,
            LocalDateTime created
    ) {
    }
}
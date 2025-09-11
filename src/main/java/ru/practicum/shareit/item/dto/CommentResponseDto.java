package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;

/**
 * DTO для ответа с отзывом
 */
public record CommentResponseDto(
        Long id,
        String text,
        String authorName,
        LocalDateTime created
) {
}
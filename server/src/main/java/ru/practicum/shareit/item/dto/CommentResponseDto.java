package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;

/**
 * DTO для ответа с отзывом.
 *
 * @param id         уникальный идентификатор комментария
 * @param text       текст комментария
 * @param authorName имя автора комментария
 * @param created    дата и время создания комментария
 */
public record CommentResponseDto(
        Long id,
        String text,
        String authorName,
        LocalDateTime created
) {
}
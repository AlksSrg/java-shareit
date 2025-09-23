package ru.practicum.shareit.server.item.dto;

/**
 * DTO для создания отзыва на сервере.
 *
 * @param text текст комментария
 */
public record CommentCreateRequestDto(
        String text
) {
}
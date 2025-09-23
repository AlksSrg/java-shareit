package ru.practicum.shareit.item.dto;

/**
 * DTO для создания отзыва на сервере.
 *
 * @param text текст комментария
 */
public record CommentCreateRequestDto(
        String text
) {
}
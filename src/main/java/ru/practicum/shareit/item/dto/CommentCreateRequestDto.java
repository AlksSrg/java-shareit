package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * DTO для создания отзыва
 */
public record CommentCreateRequestDto(
        @NotEmpty String text
) {
}
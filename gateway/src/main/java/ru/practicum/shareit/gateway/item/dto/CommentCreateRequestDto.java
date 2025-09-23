package ru.practicum.shareit.gateway.item.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * DTO для создания комментария к вещи.
 * Содержит валидационные аннотации для обязательных полей.
 */
public record CommentCreateRequestDto(
        @NotEmpty(message = "Текст комментария не может быть пустым")
        String text
) {
}
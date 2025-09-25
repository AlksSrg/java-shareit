package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для запроса вещи.
 * Содержит информацию о запросе вещи, включая ответы на него.
 * Используется для передачи данных между клиентом и сервером.
 */

@Builder
public record ItemRequestDto(
        /**
         * Уникальный идентификатор запроса
         */
        Long id,

        /**
         * Текст запроса, содержащий описание требуемой вещи
         */
        @NotEmpty(message = "Описание запроса не может быть пустым")
        String description,

        /**
         * Дата и время создания запроса
         */
        LocalDateTime created,

        /**
         * Список ответов на запрос в формате DTO
         */
        List<ItemResponseDto> items
) {
}
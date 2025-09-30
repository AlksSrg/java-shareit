package ru.practicum.shareit.request.dto;

import lombok.Builder;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для запроса вещи на сервере.
 * Содержит информацию о запросе вещи, включая ответы на него.
 */

@Builder
public record ItemRequestDto(
        /**
         * Уникальный идентификатор запроса.
         */
        Long id,

        /**
         * Текст запроса, содержащий описание требуемой вещи.
         */
        String description,

        /**
         * Дата и время создания запроса.
         */
        LocalDateTime created,

        /**
         * Список ответов на запрос в формате DTO.
         */
        List<ItemResponseDto> items
) {
}
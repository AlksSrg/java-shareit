package ru.practicum.shareit.server.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для запроса вещи на сервере.
 * Содержит информацию о запросе вещи, включая ответы на него.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    /**
     * Уникальный идентификатор запроса.
     */
    private Long id;

    /**
     * Текст запроса, содержащий описание требуемой вещи.
     */
    private String description;

    /**
     * Дата и время создания запроса.
     */
    private LocalDateTime created;

    /**
     * Список ответов на запрос в формате DTO.
     */
    private List<ItemResponseDto> items;
}
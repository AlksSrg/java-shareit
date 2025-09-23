package ru.practicum.shareit.gateway.request.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для запроса вещи.
 * Содержит информацию о запросе вещи, включая ответы на него.
 * Используется для передачи данных между клиентом и сервером.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    /**
     * Уникальный идентификатор запроса
     */
    private Long id;

    /**
     * Текст запроса, содержащий описание требуемой вещи
     */
    @NotEmpty(message = "Описание запроса не может быть пустым")
    private String description;

    /**
     * Дата и время создания запроса
     */
    private LocalDateTime created;

    /**
     * Список ответов на запрос в формате DTO
     */
    private List<ItemResponseDto> items;
}
package ru.practicum.shareit.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.user.dto.UserResponseDto;

import java.util.List;

/**
 * DTO для ответа с информацией о вещи.
 * Содержит полную информацию о вещи включая владельца, комментарии и информацию о бронированиях.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserResponseDto owner;
    private Long requestId;
    private List<CommentResponseDto> comments;
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;

    /**
     * Преобразует DTO ответа в базовый DTO.
     *
     * @return базовый DTO вещи
     */
    public ItemBaseDto toBaseDto() {
        return new ItemBaseDto(name, description, available, requestId);
    }
}
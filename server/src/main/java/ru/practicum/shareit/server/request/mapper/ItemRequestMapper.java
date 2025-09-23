package ru.practicum.shareit.server.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.model.ItemRequest;

/**
 * Маппер для преобразования между сущностью ItemRequest и DTO.
 * Использует MapStruct для автоматической генерации кода преобразования.
 */
@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    /**
     * Преобразует сущность в DTO.
     *
     * @param itemRequest сущность запроса
     * @return DTO запроса
     */
    @Mapping(target = "items", ignore = true)
    ItemRequestDto toDto(ItemRequest itemRequest);

    /**
     * Преобразует DTO в сущность.
     *
     * @param itemRequestDto DTO запроса
     * @return сущность запроса
     */
    @Mapping(target = "requestor", ignore = true)
    @Mapping(target = "created", ignore = true)
    ItemRequest toEntity(ItemRequestDto itemRequestDto);
}
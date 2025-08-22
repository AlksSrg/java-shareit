package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;
import ru.practicum.shareit.item.model.Item;

/**
 * Маппер для преобразования между DTO и сущностью Item.
 * Использует MapStruct для автоматического генерации кода преобразования.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {

    /**
     * Преобразует DTO создания в сущность Item.
     *
     * @param dto DTO для создания вещи
     * @return сущность Item
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Item toEntity(ItemCreateRequestDto dto);

    /**
     * Преобразует сущность Item в DTO ответа.
     *
     * @param item сущность вещи
     * @return DTO ответа
     */
    ItemResponseDto toResponseDto(Item item);

    /**
     * Обновляет сущность Item данными из DTO обновления.
     *
     * @param dto    DTO с данными для обновления
     * @param entity сущность для обновления
     */
    void updateEntity(ItemUpdateRequestDto dto, @MappingTarget Item entity);
}
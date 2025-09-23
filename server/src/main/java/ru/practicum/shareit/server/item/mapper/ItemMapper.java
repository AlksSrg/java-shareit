package ru.practicum.shareit.server.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.server.item.dto.*;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.model.Item;

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
    @Mapping(target = "requestId", source = "requestId")
    Item toEntity(ItemCreateRequestDto dto);

    /**
     * Преобразует сущность Item в DTO ответа.
     *
     * @param item сущность вещи
     * @return DTO ответа
     */
    @Mapping(source = "owner", target = "owner")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    ItemResponseDto toResponseDto(Item item);

    /**
     * Обновляет сущность Item данными из DTO обновления.
     *
     * @param dto    DTO с данными для обновления
     * @param entity сущность для обновления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "requestId", ignore = true)
    void updateEntity(ItemUpdateRequestDto dto, @MappingTarget Item entity);

    /**
     * Преобразует сущность Comment в DTO ответа.
     *
     * @param comment сущность комментария
     * @return DTO ответа с комментарием
     */
    @Mapping(source = "author.name", target = "authorName")
    CommentResponseDto toCommentResponseDto(Comment comment);

    /**
     * Преобразует DTO создания в сущность Comment.
     *
     * @param dto DTO для создания комментария
     * @return сущность Comment
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "created", ignore = true)
    Comment toCommentEntity(CommentCreateRequestDto dto);
}
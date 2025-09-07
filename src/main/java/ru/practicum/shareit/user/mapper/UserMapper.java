package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.model.User;

/**
 * Маппер для преобразования между DTO и сущностью User.
 * Использует MapStruct для автоматического генерации кода преобразования.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * Преобразует DTO создания в сущность User.
     *
     * @param dto DTO для создания пользователя
     * @return сущность User
     */
    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreateRequestDto dto);

    /**
     * Преобразует сущность User в DTO ответа.
     *
     * @param user сущность пользователя
     * @return DTO ответа
     */
    UserResponseDto toResponseDto(User user);

    /**
     * Обновляет сущность User данными из DTO обновления.
     *
     * @param dto    DTO с данными для обновления
     * @param entity сущность для обновления
     */
    @Mapping(target = "id", ignore = true)
    void updateEntity(UserUpdateRequestDto dto, @MappingTarget User entity);
}
package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Предоставляет бизнес-логику для операций с пользователями.
 */
public interface UserService {

    /**
     * Создает нового пользователя.
     *
     * @param userCreateRequestDto DTO с данными для создания пользователя
     * @return созданный пользователь в формате DTO
     */
    UserResponseDto create(UserCreateRequestDto userCreateRequestDto);

    /**
     * Обновляет существующего пользователя.
     *
     * @param id                   идентификатор пользователя
     * @param userUpdateRequestDto DTO с данными для обновления
     * @return обновленный пользователь в формате DTO
     */
    UserResponseDto update(Long id, UserUpdateRequestDto userUpdateRequestDto);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     */
    void delete(Long id);

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь в формате DTO
     */
    UserResponseDto findById(Long id);

    /**
     * Находит пользователя по идентификатору или выбрасывает исключение если не найден.
     *
     * @param id идентификатор пользователя
     * @return сущность пользователя
     */
    User getUserOrThrow(Long id);

    /**
     * Находит всех пользователей.
     *
     * @return список всех пользователей
     */
    List<UserResponseDto> findAll();
}
package ru.practicum.shareit.server.user.service;

import ru.practicum.shareit.server.exception.user.EmailAlreadyExistsException;
import ru.practicum.shareit.server.exception.user.UserNotFoundException;
import ru.practicum.shareit.server.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.server.user.dto.UserResponseDto;
import ru.practicum.shareit.server.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Предоставляет бизнес-логику для операций с пользователями.
 */
public interface UserService {

    /**
     * Создает нового пользователя в системе.
     *
     * @param userCreateRequestDto DTO с данными для создания пользователя
     * @return созданный пользователь в формате DTO
     * @throws EmailAlreadyExistsException если пользователь с таким email уже существует
     */
    UserResponseDto create(UserCreateRequestDto userCreateRequestDto);

    /**
     * Обновляет существующего пользователя.
     *
     * @param id                   идентификатор пользователя для обновления
     * @param userUpdateRequestDto DTO с данными для обновления
     * @return обновленный пользователь в формате DTO
     * @throws UserNotFoundException       если пользователь не найден
     * @throws EmailAlreadyExistsException если новый email уже занят другим пользователем
     */
    UserResponseDto update(Long id, UserUpdateRequestDto userUpdateRequestDto);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    void delete(Long id);

    /**
     * Находит пользователя по идентификатору и возвращает в формате DTO.
     *
     * @param id идентификатор пользователя
     * @return пользователь в формате DTO
     * @throws UserNotFoundException если пользователь не найден
     */
    UserResponseDto findById(Long id);

    /**
     * Находит пользователя по идентификатору или выбрасывает исключение если не найден.
     *
     * @param id идентификатор пользователя
     * @return сущность пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    User getUserOrThrow(Long id);

    /**
     * Находит всех пользователей в системе.
     *
     * @return список всех пользователей в формате DTO
     */
    List<UserResponseDto> findAll();
}
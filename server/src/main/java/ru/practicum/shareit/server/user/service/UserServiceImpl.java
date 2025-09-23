package ru.practicum.shareit.server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.exception.user.EmailAlreadyExistsException;
import ru.practicum.shareit.server.exception.user.UserNotFoundException;
import ru.practicum.shareit.server.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.server.user.dto.UserResponseDto;
import ru.practicum.shareit.server.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.server.user.mapper.UserMapper;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.util.List;

/**
 * Реализация сервиса для работы с пользователями.
 * Обеспечивает бизнес-логику операций CRUD с пользователями.
 * Включает валидацию уникальности email и обработку исключений.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Создает нового пользователя в системе.
     *
     * @param userCreateRequestDto DTO с данными для создания пользователя
     * @return созданный пользователь в формате DTO
     * @throws EmailAlreadyExistsException если пользователь с таким email уже существует
     */
    @Override
    @Transactional
    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        log.info("Создание пользователя: {}", userCreateRequestDto.email());

        validateEmailUniqueness(null, userCreateRequestDto.email());

        User user = userMapper.toEntity(userCreateRequestDto);
        User savedUser = userRepository.save(user);

        log.debug("Пользователь создан с ID: {}", savedUser.getId());
        return userMapper.toResponseDto(savedUser);
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param id                   идентификатор пользователя для обновления
     * @param userUpdateRequestDto DTO с данными для обновления
     * @return обновленный пользователь в формате DTO
     * @throws UserNotFoundException       если пользователь не найден
     * @throws EmailAlreadyExistsException если новый email уже занят другим пользователем
     */
    @Override
    @Transactional
    public UserResponseDto update(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Обновление пользователя с ID: {}", id);

        User user = getUserOrThrow(id);

        if (userUpdateRequestDto.email() != null && !userUpdateRequestDto.email().isBlank()) {
            validateEmailUniqueness(id, userUpdateRequestDto.email());
        }

        userMapper.updateEntity(userUpdateRequestDto, user);
        User updatedUser = userRepository.save(user);

        log.debug("Пользователь с ID {} обновлен", id);
        return userMapper.toResponseDto(updatedUser);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Удаление пользователя с ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Пользователь с id=" + id + " не найден");
        }

        userRepository.deleteById(id);
        log.debug("Пользователь с ID {} удален", id);
    }

    /**
     * Находит пользователя по идентификатору и возвращает в формате DTO.
     *
     * @param id идентификатор пользователя
     * @return пользователь в формате DTO
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public UserResponseDto findById(Long id) {
        log.debug("Поиск пользователя с ID: {}", id);

        User user = getUserOrThrow(id);
        return userMapper.toResponseDto(user);
    }

    /**
     * Находит пользователя по идентификатору или выбрасывает исключение если не найден.
     *
     * @param id идентификатор пользователя
     * @return сущность пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + id + " не найден"));
    }

    /**
     * Находит всех пользователей в системе.
     *
     * @return список всех пользователей в формате DTO
     */
    @Override
    public List<UserResponseDto> findAll() {
        log.debug("Получение списка всех пользователей");

        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    /**
     * Проверяет уникальность email.
     * Если email уже существует у другого пользователя, выбрасывает исключение.
     *
     * @param userId идентификатор текущего пользователя (null при создании)
     * @param email  email для проверки
     * @throws EmailAlreadyExistsException если email уже существует у другого пользователя
     */
    private void validateEmailUniqueness(Long userId, String email) {
        String normalizedEmail = email.trim().toLowerCase();

        userRepository.findByEmailIgnoreCase(normalizedEmail)
                .ifPresent(existingUser -> {
                    if (userId == null || !existingUser.getId().equals(userId)) {
                        throw new EmailAlreadyExistsException(
                                "Пользователь с email='" + email + "' уже существует"
                        );
                    }
                });
    }
}
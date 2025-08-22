package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.user.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.user.UserNotFound;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

/**
 * Реализация сервиса для работы с пользователями.
 * Обеспечивает бизнес-логику операций CRUD с пользователями.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Создает нового пользователя.
     *
     * @param userCreateRequestDto DTO с данными для создания пользователя
     * @return созданный пользователь в формате DTO
     */
    @Override
    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        // Проверка на существование пользователя с таким email
        checkEmailUniqueness(null, userCreateRequestDto.email());

        User user = userMapper.toEntity(userCreateRequestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param id                   идентификатор пользователя
     * @param userUpdateRequestDto DTO с данными для обновления
     * @return обновленный пользователь в формате DTO
     */
    @Override
    public UserResponseDto update(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = getUserOrThrow(id);

        // Проверка на уникальность email, если он указан для обновления
        if (userUpdateRequestDto.email() != null && !userUpdateRequestDto.email().isBlank()) {
            checkEmailUniqueness(id, userUpdateRequestDto.email());
        }

        userMapper.updateEntity(userUpdateRequestDto, user);
        User updatedUser = userRepository.save(user);

        return userMapper.toResponseDto(updatedUser);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     */
    @Override
    public void delete(Long id) {
        getUserOrThrow(id);
        userRepository.deleteById(id);
    }

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь в формате DTO
     */
    @Override
    public UserResponseDto findById(Long id) {
        User user = getUserOrThrow(id);
        return userMapper.toResponseDto(user);
    }

    /**
     * Находит пользователя по идентификатору или выбрасывает исключение если не найден.
     *
     * @param id идентификатор пользователя
     * @return сущность пользователя
     */
    @Override
    public User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("Пользователь с id=" + id + " не найден"));
    }

    /**
     * Находит всех пользователей.
     *
     * @return список всех пользователей
     */
    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    /**
     * Проверяет уникальность email.
     *
     * @param userId id текущего пользователя (null при создании)
     * @param email  email для проверки
     * @throws EmailAlreadyExistsException если email уже существует у другого пользователя
     */
    private void checkEmailUniqueness(Long userId, String email) {
        userRepository.findByEmail(email)
                .ifPresent(existingUser -> {
                    if (userId == null || !existingUser.getId().equals(userId)) {
                        throw new EmailAlreadyExistsException(
                                "Пользователь с email='" + email + "' уже существует"
                        );
                    }
                });
    }
}
package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * Контроллер для работы с пользователями.
 * Обеспечивает REST API для управления информацией о пользователях.
 * Поддерживает операции:
 * - CRUD операции с пользователями
 * Все методы работают с сущностью {@link User} и используют {@link UserService} для бизнес-логики.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    /**
     * Создает нового пользователя.
     *
     * @param userCreateRequestDto DTO с данными для создания пользователя
     * @return созданный пользователь в формате DTO
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        return userService.create(userCreateRequestDto);
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param id                   идентификатор пользователя
     * @param userUpdateRequestDto DTO с данными для обновления
     * @return обновленный пользователь в формате DTO
     */
    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return userService.update(id, userUpdateRequestDto);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь в формате DTO
     */
    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    /**
     * Получает всех пользователей.
     *
     * @return список всех пользователей
     */
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.findAll();
    }
}
package ru.practicum.shareit.server.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.server.user.dto.UserResponseDto;
import ru.practicum.shareit.server.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.server.user.service.UserService;

import java.util.List;

/**
 * Контроллер для работы с пользователями на сервере.
 * Обеспечивает REST API для управления информацией о пользователях.
 * Выполняет бизнес-логику без валидации входных данных.
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
    @PostMapping
    public UserResponseDto createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
        return userService.create(userCreateRequestDto);
    }

    /**
     * Обновляет существующего пользователя через PATCH.
     *
     * @param id                   идентификатор пользователя
     * @param userUpdateRequestDto DTO с данными для обновления
     * @return обновленный пользователь в формате DTO
     */
    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return userService.update(id, userUpdateRequestDto);
    }

    /**
     * Обновляет существующего пользователя через POST (для gateway).
     *
     * @param id                   идентификатор пользователя
     * @param userUpdateRequestDto DTO с данными для обновления
     * @return обновленный пользователь в формате DTO
     */
    @PostMapping("/{id}")
    public UserResponseDto updateUserPost(@PathVariable Long id,
                                          @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return userService.update(id, userUpdateRequestDto);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     */
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
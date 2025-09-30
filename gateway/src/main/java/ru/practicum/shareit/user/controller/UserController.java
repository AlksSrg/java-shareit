package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

/**
 * Контроллер для работы с пользователями на gateway.
 * Обеспечивает REST API для управления информацией о пользователях.
 * Выполняет валидацию входных данных и проксирует запросы на основной сервер.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    /**
     * Создает нового пользователя.
     *
     * @param userCreateRequestDto DTO с данными для создания пользователя
     * @return созданный пользователь
     */
    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        return userClient.create(userCreateRequestDto);
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param id                   идентификатор пользователя для обновления
     * @param userUpdateRequestDto DTO с данными для обновления
     * @return обновленный пользователь
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,
                                             @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return userClient.update(id, userUpdateRequestDto);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя для удаления
     * @return статус операции удаления
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userClient.delete(id);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return информация о пользователе
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        return userClient.findById(id);
    }

    /**
     * Получает всех пользователей.
     *
     * @return список всех пользователей
     */
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.findAll();
    }
}
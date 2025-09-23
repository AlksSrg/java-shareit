package ru.practicum.shareit.server.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.exception.user.EmailAlreadyExistsException;
import ru.practicum.shareit.server.exception.user.UserNotFoundException;
import ru.practicum.shareit.server.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.server.user.dto.UserResponseDto;
import ru.practicum.shareit.server.user.dto.UserUpdateRequestDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Sql(scripts = {"/test-schema.sql", "/test-data.sql"})
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void create_shouldCreateUserSuccessfully() {
        UserCreateRequestDto createDto = new UserCreateRequestDto("New User", "newuser@example.com");

        UserResponseDto createdUser = userService.create(createDto);

        assertNotNull(createdUser);
        assertNotNull(createdUser.id());
        assertEquals("New User", createdUser.name());
        assertEquals("newuser@example.com", createdUser.email());
    }

    @Test
    void create_shouldThrowExceptionWhenEmailAlreadyExists() {
        UserCreateRequestDto createDto = new UserCreateRequestDto("Duplicate", "owner@example.com");

        assertThrows(EmailAlreadyExistsException.class, () -> userService.create(createDto));
    }

    @Test
    void update_shouldUpdateUserSuccessfully() {
        Long userId = 1L;
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Updated Name", "updated@example.com");

        UserResponseDto updatedUser = userService.update(userId, updateDto);

        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.id());
        assertEquals("Updated Name", updatedUser.name());
        assertEquals("updated@example.com", updatedUser.email());
    }

    @Test
    void update_shouldUpdateOnlyNameWhenEmailIsNull() {
        Long userId = 1L;
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Updated Name Only", null);

        UserResponseDto updatedUser = userService.update(userId, updateDto);

        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.id());
        assertEquals("Updated Name Only", updatedUser.name());
        assertEquals("owner@example.com", updatedUser.email());
    }

    @Test
    void update_shouldUpdateOnlyEmailWhenNameIsNull() {
        Long userId = 1L;
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto(null, "onlyemail@example.com");

        UserResponseDto updatedUser = userService.update(userId, updateDto);

        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.id());
        assertEquals("Owner User", updatedUser.name());
        assertEquals("onlyemail@example.com", updatedUser.email());
    }

    @Test
    void update_shouldThrowExceptionWhenUserNotFound() {
        Long nonExistentUserId = 999L;
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Name", "email@example.com");

        assertThrows(UserNotFoundException.class, () -> userService.update(nonExistentUserId, updateDto));
    }

    @Test
    void update_shouldThrowExceptionWhenEmailAlreadyExists() {
        Long userId = 1L;
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Name", "booker@example.com");

        assertThrows(EmailAlreadyExistsException.class, () -> userService.update(userId, updateDto));
    }

    @Test
    void findById_shouldReturnUserWhenExists() {
        Long userId = 1L;

        UserResponseDto user = userService.findById(userId);

        assertNotNull(user);
        assertEquals(userId, user.id());
        assertEquals("Owner User", user.name());
        assertEquals("owner@example.com", user.email());
    }

    @Test
    void findById_shouldThrowExceptionWhenUserNotFound() {
        Long nonExistentUserId = 999L;

        assertThrows(UserNotFoundException.class, () -> userService.findById(nonExistentUserId));
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        List<UserResponseDto> users = userService.findAll();

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertTrue(users.size() >= 3);
    }

    @Test
    void delete_shouldDeleteUserSuccessfully() {
        Long userId = 3L;

        userService.delete(userId);

        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    void delete_shouldThrowExceptionWhenUserNotFound() {
        Long nonExistentUserId = 999L;

        assertThrows(UserNotFoundException.class, () -> userService.delete(nonExistentUserId));
    }

    @Test
    void getUserOrThrow_shouldReturnUserWhenExists() {
        Long userId = 1L;

        var user = userService.getUserOrThrow(userId);

        assertNotNull(user);
        assertEquals(userId, user.getId());
    }

    @Test
    void getUserOrThrow_shouldThrowExceptionWhenUserNotFound() {
        Long nonExistentUserId = 999L;

        assertThrows(UserNotFoundException.class, () -> userService.getUserOrThrow(nonExistentUserId));
    }
}
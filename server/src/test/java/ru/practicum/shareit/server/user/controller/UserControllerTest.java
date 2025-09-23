package ru.practicum.shareit.server.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.user.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.user.UserNotFoundException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        UserCreateRequestDto createDto = new UserCreateRequestDto("Test User", "test@example.com");
        UserResponseDto responseDto = new UserResponseDto(1L, "Test User", "test@example.com");

        Mockito.when(userService.create(any(UserCreateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void createUser_shouldReturnConflictWhenEmailExists() throws Exception {
        UserCreateRequestDto createDto = new UserCreateRequestDto("Test User", "duplicate@example.com");

        Mockito.when(userService.create(any(UserCreateRequestDto.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already exists"));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Updated User", "updated@example.com");
        UserResponseDto responseDto = new UserResponseDto(1L, "Updated User", "updated@example.com");

        Mockito.when(userService.update(eq(1L), any(UserUpdateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void updateUserPost_shouldReturnUpdatedUser() throws Exception {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Updated User", "updated@example.com");
        UserResponseDto responseDto = new UserResponseDto(1L, "Updated User", "updated@example.com");

        Mockito.when(userService.update(eq(1L), any(UserUpdateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void updateUser_shouldReturnNotFoundWhenUserNotExists() throws Exception {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Updated User", "updated@example.com");

        Mockito.when(userService.update(eq(999L), any(UserUpdateRequestDto.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(patch("/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {
        UserResponseDto responseDto = new UserResponseDto(1L, "Test User", "test@example.com");

        Mockito.when(userService.findById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUser_shouldReturnNotFoundWhenUserNotExists() throws Exception {
        Mockito.when(userService.findById(999L))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() throws Exception {
        List<UserResponseDto> users = List.of(
                new UserResponseDto(1L, "User 1", "user1@example.com"),
                new UserResponseDto(2L, "User 2", "user2@example.com")
        );

        Mockito.when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("User 2"));
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(userService).delete(1L);
    }

    @Test
    void deleteUser_shouldReturnNotFoundWhenUserNotExists() throws Exception {
        Mockito.doThrow(new UserNotFoundException("User not found"))
                .when(userService).delete(999L);

        mockMvc.perform(delete("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }
}
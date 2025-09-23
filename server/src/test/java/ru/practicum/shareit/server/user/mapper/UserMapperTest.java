package ru.practicum.shareit.server.user.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.server.user.dto.UserResponseDto;
import ru.practicum.shareit.server.user.dto.UserUpdateRequestDto;
import ru.practicum.shareit.server.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity_shouldHandleNullCreateRequestDto() {
        User user = userMapper.toEntity(null);

        assertThat(user).isNull();
    }

    @Test
    void toResponseDto_shouldHandleNullEntity() {
        UserResponseDto dto = userMapper.toResponseDto(null);

        assertThat(dto).isNull();
    }

    @Test
    void updateEntity_shouldHandleNullUpdateDto() {
        User user = User.builder()
                .id(1L)
                .name("Original Name")
                .email("original@example.com")
                .build();

        userMapper.updateEntity(null, user);

        assertThat(user.getName()).isEqualTo("Original Name");
        assertThat(user.getEmail()).isEqualTo("original@example.com");
    }

    @Test
    void updateEntity_shouldThrowExceptionWhenTargetEntityIsNull() {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("New Name", "new@example.com");

        assertThrows(NullPointerException.class, () -> {
            userMapper.updateEntity(updateDto, null);
        });
    }

    @Test
    void updateEntity_shouldHandlePartialUpdate() {
        User user = User.builder()
                .id(1L)
                .name("Original Name")
                .email("original@example.com")
                .build();

        UserUpdateRequestDto partialUpdate = new UserUpdateRequestDto("Updated Name", null);

        userMapper.updateEntity(partialUpdate, user);

        assertThat(user.getName()).isEqualTo("Updated Name");
        assertThat(user.getEmail()).isEqualTo("original@example.com");
    }

    @Test
    void updateEntity_shouldHandleEmptyStrings() {
        User user = User.builder()
                .id(1L)
                .name("Original Name")
                .email("original@example.com")
                .build();

        UserUpdateRequestDto updateWithEmpty = new UserUpdateRequestDto("", "");

        userMapper.updateEntity(updateWithEmpty, user);

        assertThat(user.getName()).isEqualTo("");
        assertThat(user.getEmail()).isEqualTo("");
    }
}
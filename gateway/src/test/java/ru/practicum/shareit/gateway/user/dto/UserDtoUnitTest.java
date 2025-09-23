package ru.practicum.shareit.gateway.user.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoUnitTest {

    @Test
    void userCreateRequestDto_toBaseDto() {
        UserCreateRequestDto createDto = new UserCreateRequestDto("Test User", "test@email.com");
        UserBaseDto baseDto = createDto.toBaseDto();

        assertThat(baseDto.name()).isEqualTo("Test User");
        assertThat(baseDto.email()).isEqualTo("test@email.com");
    }

    @Test
    void userUpdateRequestDto_toBaseDto() {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Updated User", "updated@email.com");
        UserBaseDto baseDto = updateDto.toBaseDto();

        assertThat(baseDto.name()).isEqualTo("Updated User");
        assertThat(baseDto.email()).isEqualTo("updated@email.com");
    }

    @Test
    void userResponseDto_toBaseDto() {
        UserResponseDto responseDto = new UserResponseDto(1L, "Test User", "test@email.com");
        UserBaseDto baseDto = responseDto.toBaseDto();

        assertThat(baseDto.name()).isEqualTo("Test User");
        assertThat(baseDto.email()).isEqualTo("test@email.com");
    }

    @Test
    void userBaseDto_constructorAndGetters() {
        UserBaseDto baseDto = new UserBaseDto("Test User", "test@email.com");

        assertThat(baseDto.name()).isEqualTo("Test User");
        assertThat(baseDto.email()).isEqualTo("test@email.com");
    }
}
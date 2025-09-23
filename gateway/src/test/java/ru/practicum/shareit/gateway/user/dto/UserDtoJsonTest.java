package ru.practicum.shareit.gateway.user.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserCreateRequestDto> createJson;

    @Autowired
    private JacksonTester<UserUpdateRequestDto> updateJson;

    @Autowired
    private JacksonTester<UserResponseDto> responseJson;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void userCreateRequestDto_serialization() throws Exception {
        UserCreateRequestDto dto = new UserCreateRequestDto("Test User", "test@email.com");

        String jsonContent = createJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"name\":\"Test User\"");
        assertThat(jsonContent).contains("\"email\":\"test@email.com\"");
    }

    @Test
    void userCreateRequestDto_deserialization() throws Exception {
        String json = "{\"name\":\"Test User\",\"email\":\"test@email.com\"}";

        UserCreateRequestDto dto = createJson.parse(json).getObject();

        assertThat(dto.name()).isEqualTo("Test User");
        assertThat(dto.email()).isEqualTo("test@email.com");
    }

    @Test
    void userCreateRequestDto_validation() {
        UserCreateRequestDto validDto = new UserCreateRequestDto("Test User", "test@email.com");
        UserCreateRequestDto invalidDto = new UserCreateRequestDto("", "invalid-email");

        Set<ConstraintViolation<UserCreateRequestDto>> validViolations = validator.validate(validDto);
        Set<ConstraintViolation<UserCreateRequestDto>> invalidViolations = validator.validate(invalidDto);

        assertThat(validViolations).isEmpty();
        assertThat(invalidViolations).hasSize(2);
    }

    @Test
    void userUpdateRequestDto_serialization() throws Exception {
        UserUpdateRequestDto dto = new UserUpdateRequestDto("Updated User", "updated@email.com");

        String jsonContent = updateJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"name\":\"Updated User\"");
        assertThat(jsonContent).contains("\"email\":\"updated@email.com\"");
    }

    @Test
    void userUpdateRequestDto_validation() {
        UserUpdateRequestDto validDto = new UserUpdateRequestDto("Updated User", "updated@email.com");
        UserUpdateRequestDto invalidDto = new UserUpdateRequestDto("Updated User", "invalid-email");

        Set<ConstraintViolation<UserUpdateRequestDto>> validViolations = validator.validate(validDto);
        Set<ConstraintViolation<UserUpdateRequestDto>> invalidViolations = validator.validate(invalidDto);

        assertThat(validViolations).isEmpty();
        assertThat(invalidViolations).hasSize(1);
    }

    @Test
    void userResponseDto_serialization() throws Exception {
        UserResponseDto dto = new UserResponseDto(1L, "Test User", "test@email.com");

        String jsonContent = responseJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"name\":\"Test User\"");
        assertThat(jsonContent).contains("\"email\":\"test@email.com\"");
    }

    @Test
    void userResponseDto_deserialization() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@email.com\"}";

        UserResponseDto dto = responseJson.parse(json).getObject();

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Test User");
        assertThat(dto.email()).isEqualTo("test@email.com");
    }
}
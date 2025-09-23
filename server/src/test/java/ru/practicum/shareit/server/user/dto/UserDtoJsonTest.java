package ru.practicum.shareit.server.user.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.server.user.model.User;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserCreateRequestDto> createRequestJson;

    @Autowired
    private JacksonTester<UserResponseDto> responseJson;

    @Autowired
    private JacksonTester<UserUpdateRequestDto> updateRequestJson;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void userCreateRequestDto_shouldSerializeCorrectly() throws Exception {
        UserCreateRequestDto dto = new UserCreateRequestDto("Test User", "test@example.com");

        JsonContent<UserCreateRequestDto> json = createRequestJson.write(dto);

        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test User");
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("test@example.com");
    }

    @Test
    void userCreateRequestDto_shouldDeserializeCorrectly() throws Exception {
        String json = "{\"name\":\"Test User\",\"email\":\"test@example.com\"}";

        UserCreateRequestDto dto = objectMapper.readValue(json, UserCreateRequestDto.class);

        assertThat(dto.name()).isEqualTo("Test User");
        assertThat(dto.email()).isEqualTo("test@example.com");
    }

    @Test
    void userResponseDto_shouldSerializeCorrectly() throws Exception {
        UserResponseDto dto = new UserResponseDto(1L, "Test User", "test@example.com");

        JsonContent<UserResponseDto> json = responseJson.write(dto);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test User");
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("test@example.com");
    }

    @Test
    void userResponseDto_shouldDeserializeCorrectly() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@example.com\"}";

        UserResponseDto dto = objectMapper.readValue(json, UserResponseDto.class);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Test User");
        assertThat(dto.email()).isEqualTo("test@example.com");
    }

    @Test
    void userUpdateRequestDto_shouldSerializeWithNullFields() throws Exception {
        UserUpdateRequestDto dto = new UserUpdateRequestDto(null, "updated@example.com");

        JsonContent<UserUpdateRequestDto> json = updateRequestJson.write(dto);

        assertThat(json).extractingJsonPathStringValue("$.name").isNull();
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("updated@example.com");
    }

    @Test
    void userUpdateRequestDto_shouldDeserializeWithNullFields() throws Exception {
        String json = "{\"name\":null,\"email\":\"updated@example.com\"}";

        UserUpdateRequestDto dto = objectMapper.readValue(json, UserUpdateRequestDto.class);

        assertThat(dto.name()).isNull();
        assertThat(dto.email()).isEqualTo("updated@example.com");
    }

    @Test
    void userBaseDto_toBaseDtoMethods_shouldWorkCorrectly() {
        UserCreateRequestDto createDto = new UserCreateRequestDto("Create User", "create@example.com");
        UserResponseDto responseDto = new UserResponseDto(1L, "Response User", "response@example.com");
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto("Update User", "update@example.com");

        UserBaseDto createBase = createDto.toBaseDto();
        UserBaseDto responseBase = responseDto.toBaseDto();
        UserBaseDto updateBase = updateDto.toBaseDto();

        assertThat(createBase.name()).isEqualTo("Create User");
        assertThat(createBase.email()).isEqualTo("create@example.com");

        assertThat(responseBase.name()).isEqualTo("Response User");
        assertThat(responseBase.email()).isEqualTo("response@example.com");

        assertThat(updateBase.name()).isEqualTo("Update User");
        assertThat(updateBase.email()).isEqualTo("update@example.com");
    }

    @Test
    void userDto_validation_shouldWorkForUserEntity() {
        User invalidUser = new User();
        invalidUser.setName("");
        invalidUser.setEmail("invalid-email");

        Set<ConstraintViolation<User>> violations = validator.validate(invalidUser);

        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .contains("Имя пользователя не может быть пустым")
                .contains("Email должен быть валидным адресом");
    }

    @Test
    void userDto_serializationWithSpecialCharacters() throws JsonProcessingException {
        UserCreateRequestDto dto = new UserCreateRequestDto("User with spéciål chàrs", "test+special@example.com");

        String json = objectMapper.writeValueAsString(dto);
        UserCreateRequestDto deserialized = objectMapper.readValue(json, UserCreateRequestDto.class);

        assertThat(deserialized.name()).isEqualTo("User with spéciål chàrs");
        assertThat(deserialized.email()).isEqualTo("test+special@example.com");
    }
}
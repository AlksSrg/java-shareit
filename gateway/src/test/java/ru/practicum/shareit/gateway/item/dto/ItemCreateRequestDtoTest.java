package ru.practicum.shareit.gateway.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemCreateRequestDtoTest {

    @Autowired
    private JacksonTester<ItemCreateRequestDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testSerialize() throws Exception {
        ItemCreateRequestDto dto = new ItemCreateRequestDto("Item", "Description", true, 1L);

        JsonContent<ItemCreateRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"name\":\"Item\",\"description\":\"Description\",\"available\":true,\"requestId\":1}";

        ItemCreateRequestDto result = objectMapper.readValue(content, ItemCreateRequestDto.class);

        assertThat(result.name()).isEqualTo("Item");
        assertThat(result.description()).isEqualTo("Description");
        assertThat(result.available()).isTrue();
        assertThat(result.requestId()).isEqualTo(1L);
    }

    @Test
    void shouldValidate_WhenAllRequiredFieldsArePresent() {
        ItemCreateRequestDto validDto = new ItemCreateRequestDto("Item", "Description", true, null);

        Set<ConstraintViolation<ItemCreateRequestDto>> violations = validator.validate(validDto);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldNotValidate_WhenNameIsEmpty() {
        ItemCreateRequestDto invalidDto = new ItemCreateRequestDto("", "Description", true, null);

        Set<ConstraintViolation<ItemCreateRequestDto>> violations = validator.validate(invalidDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Название вещи не может быть пустым");
    }

    @Test
    void shouldNotValidate_WhenDescriptionIsEmpty() {
        ItemCreateRequestDto invalidDto = new ItemCreateRequestDto("Item", "", true, null);

        Set<ConstraintViolation<ItemCreateRequestDto>> violations = validator.validate(invalidDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Описание вещи не может быть пустым");
    }

    @Test
    void shouldNotValidate_WhenAvailableIsNull() {
        ItemCreateRequestDto invalidDto = new ItemCreateRequestDto("Item", "Description", null, null);

        Set<ConstraintViolation<ItemCreateRequestDto>> violations = validator.validate(invalidDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Статус доступности не может быть пустым");
    }
}
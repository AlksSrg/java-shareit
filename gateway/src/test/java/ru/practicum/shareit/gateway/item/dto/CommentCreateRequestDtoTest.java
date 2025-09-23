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

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentCreateRequestDtoTest {

    @Autowired
    private JacksonTester<CommentCreateRequestDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testSerialize() throws Exception {
        CommentCreateRequestDto dto = new CommentCreateRequestDto("Great item!");

        JsonContent<CommentCreateRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Great item!");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"text\":\"Great item!\"}";

        CommentCreateRequestDto result = objectMapper.readValue(content, CommentCreateRequestDto.class);

        assertThat(result.text()).isEqualTo("Great item!");
    }

    @Test
    void shouldValidate_WhenTextIsNotEmpty() {
        CommentCreateRequestDto validDto = new CommentCreateRequestDto("Valid text");

        Set<ConstraintViolation<CommentCreateRequestDto>> violations = validator.validate(validDto);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldNotValidate_WhenTextIsEmpty() {
        CommentCreateRequestDto invalidDto = new CommentCreateRequestDto("");

        Set<ConstraintViolation<CommentCreateRequestDto>> violations = validator.validate(invalidDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Текст комментария не может быть пустым");
    }

    @Test
    void shouldNotValidate_WhenTextIsNull() {
        CommentCreateRequestDto invalidDto = new CommentCreateRequestDto(null);

        Set<ConstraintViolation<CommentCreateRequestDto>> violations = validator.validate(invalidDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Текст комментария не может быть пустым");
    }
}
package ru.practicum.shareit.gateway.booking.dto;

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
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingCreateRequestDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JacksonTester<BookingCreateRequestDto> json;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testSerialization() throws JsonProcessingException {
        BookingCreateRequestDto dto = new BookingCreateRequestDto(
                1L,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 2, 10, 0)
        );

        String jsonString = objectMapper.writeValueAsString(dto);

        assertThat(jsonString).contains("\"itemId\":1");
        assertThat(jsonString).contains("\"start\":\"2024-01-01T10:00:00\"");
        assertThat(jsonString).contains("\"end\":\"2024-01-02T10:00:00\"");
    }

    @Test
    void testDeserialization() throws Exception {
        String content = "{\"itemId\":1,\"start\":\"2024-01-01T10:00:00\",\"end\":\"2024-01-02T10:00:00\"}";

        BookingCreateRequestDto result = objectMapper.readValue(content, BookingCreateRequestDto.class);

        assertThat(result.itemId()).isEqualTo(1L);
        assertThat(result.start()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertThat(result.end()).isEqualTo(LocalDateTime.of(2024, 1, 2, 10, 0));
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        BookingCreateRequestDto dto = new BookingCreateRequestDto(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1)
        );

        Set<ConstraintViolation<BookingCreateRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenStartIsNull_thenViolation() {
        BookingCreateRequestDto dto = new BookingCreateRequestDto(
                1L,
                null,
                LocalDateTime.now().plusDays(1)
        );

        Set<ConstraintViolation<BookingCreateRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Дата начала бронирования не может быть пустой");
    }

    @Test
    void whenEndIsNull_thenViolation() {
        BookingCreateRequestDto dto = new BookingCreateRequestDto(
                1L,
                LocalDateTime.now().plusHours(1),
                null
        );

        Set<ConstraintViolation<BookingCreateRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Дата окончания бронирования не может быть пустой");
    }

    @Test
    void whenStartIsInPast_thenViolation() {
        BookingCreateRequestDto dto = new BookingCreateRequestDto(
                1L,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusDays(1)
        );

        Set<ConstraintViolation<BookingCreateRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Дата начала бронирования должна быть в настоящем или будущем");
    }

    @Test
    void whenEndIsInPast_thenViolation() {
        BookingCreateRequestDto dto = new BookingCreateRequestDto(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().minusDays(1)
        );

        Set<ConstraintViolation<BookingCreateRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Дата окончания бронирования должна быть в будущем");
    }

    @Test
    void whenEndIsBeforeStart_thenNoViolationBecauseAnnotationsDontCheckOrder() {
        BookingCreateRequestDto dto = new BookingCreateRequestDto(
                1L,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1)
        );

        Set<ConstraintViolation<BookingCreateRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void testJsonFormat() throws Exception {
        BookingCreateRequestDto dto = new BookingCreateRequestDto(
                1L,
                LocalDateTime.of(2024, 1, 1, 10, 30, 45),
                LocalDateTime.of(2024, 1, 2, 15, 45, 30)
        );

        JsonContent<BookingCreateRequestDto> jsonContent = json.write(dto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo("2024-01-01T10:30:45");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo("2024-01-02T15:45:30");
    }
}
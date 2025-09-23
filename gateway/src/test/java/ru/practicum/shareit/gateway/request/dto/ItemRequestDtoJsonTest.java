package ru.practicum.shareit.gateway.request.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testValidation() {
        ItemRequestDto dto = new ItemRequestDto(null, "", null, List.of());

        Set<ConstraintViolation<ItemRequestDto>> violations = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(dto);

        boolean hasDescriptionViolation = violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("description"));

        assertThat(hasDescriptionViolation).isTrue();
    }

    @Test
    void testDateFormat() throws Exception {
        LocalDateTime date = LocalDateTime.now();

        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("desc")
                .created(date)
                .items(List.of())
                .build();

        String content = json.write(dto).getJson();

        assertThat(content).contains(date.toString().substring(0, 23));
    }
}
package ru.practicum.shareit.gateway.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.ItemResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemResponseDtoJsonTest {

    @Autowired
    private JacksonTester<ItemResponseDto> json;

    @Test
    void testSerialization() throws Exception {
        ItemResponseDto dto = ItemResponseDto.builder()
                .id(1L)
                .name("item")
                .description("desc")
                .available(true)
                .requestId(2L)
                .build();

        String content = json.write(dto).getJson();

        assertThat(content).contains("\"id\":1");
        assertThat(content).contains("\"name\":\"item\"");
        assertThat(content).contains("\"available\":true");
    }
}

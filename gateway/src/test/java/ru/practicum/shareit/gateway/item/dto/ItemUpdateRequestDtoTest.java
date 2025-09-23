package ru.practicum.shareit.gateway.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemUpdateRequestDtoTest {

    @Autowired
    private JacksonTester<ItemUpdateRequestDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws Exception {
        ItemUpdateRequestDto dto = new ItemUpdateRequestDto("Updated", "New description", true);

        JsonContent<ItemUpdateRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Updated");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("New description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }

    @Test
    void testSerialize_WithNullFields() throws Exception {
        ItemUpdateRequestDto dto = new ItemUpdateRequestDto(null, null, null);

        JsonContent<ItemUpdateRequestDto> result = json.write(dto);

        assertThat(result).doesNotHaveJsonPathValue("$.name");
        assertThat(result).doesNotHaveJsonPathValue("$.description");
        assertThat(result).doesNotHaveJsonPathValue("$.available");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"name\":\"Updated\",\"description\":\"New description\",\"available\":true}";

        ItemUpdateRequestDto result = objectMapper.readValue(content, ItemUpdateRequestDto.class);

        assertThat(result.name()).isEqualTo("Updated");
        assertThat(result.description()).isEqualTo("New description");
        assertThat(result.available()).isTrue();
    }

    @Test
    void testDeserialize_PartialFields() throws Exception {
        String content = "{\"name\":\"Updated\"}";

        ItemUpdateRequestDto result = objectMapper.readValue(content, ItemUpdateRequestDto.class);

        assertThat(result.name()).isEqualTo("Updated");
        assertThat(result.description()).isNull();
        assertThat(result.available()).isNull();
    }

    @Test
    void testDeserialize_EmptyObject() throws Exception {
        String content = "{}";

        ItemUpdateRequestDto result = objectMapper.readValue(content, ItemUpdateRequestDto.class);

        assertThat(result.name()).isNull();
        assertThat(result.description()).isNull();
        assertThat(result.available()).isNull();
    }
}
package ru.practicum.shareit.server.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeToJson() throws Exception {
        LocalDateTime created = LocalDateTime.of(2023, 1, 1, 10, 0, 0);
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("Need a new item")
                .created(created)
                .items(Collections.emptyList())
                .build();

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"description\":\"Need a new item\"");
        assertThat(json).contains("\"created\":\"2023-01-01T10:00:00\"");
        assertThat(json).contains("\"items\":[]");
    }

    @Test
    void shouldDeserializeFromJson() throws Exception {
        String json = "{\"id\":1,\"description\":\"Need a new item\",\"created\":\"2023-01-01T10:00:00\",\"items\":[]}";

        ItemRequestDto dto = objectMapper.readValue(json, ItemRequestDto.class);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Need a new item");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 10, 0, 0));
        assertThat(dto.getItems()).isEmpty();
    }

    @Test
    void shouldHandleNullValues() throws Exception {
        String json = "{\"description\":\"Need a new item\"}";

        ItemRequestDto dto = objectMapper.readValue(json, ItemRequestDto.class);

        assertThat(dto.getId()).isNull();
        assertThat(dto.getDescription()).isEqualTo("Need a new item");
        assertThat(dto.getCreated()).isNull();
        assertThat(dto.getItems()).isNull();
    }
}
package ru.practicum.shareit.server.item.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemCreateRequestDto> createRequestJson;

    @Autowired
    private JacksonTester<ItemUpdateRequestDto> updateRequestJson;

    @Autowired
    private JacksonTester<ItemResponseDto> responseJson;

    @Autowired
    private JacksonTester<CommentCreateRequestDto> commentCreateJson;

    @Autowired
    private JacksonTester<CommentResponseDto> commentResponseJson;

    @Autowired
    private JacksonTester<ItemBaseDto> baseDtoJson;

    @Autowired
    private JacksonTester<BookingForItemDto> bookingForItemJson;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void itemCreateRequestDto_serialization() throws Exception {
        ItemCreateRequestDto dto = new ItemCreateRequestDto(
                "Test Item", "Test Description", true, 1L);

        String jsonContent = createRequestJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"name\":\"Test Item\"");
        assertThat(jsonContent).contains("\"description\":\"Test Description\"");
        assertThat(jsonContent).contains("\"available\":true");
        assertThat(jsonContent).contains("\"requestId\":1");
    }

    @Test
    void itemCreateRequestDto_deserialization() throws Exception {
        String json = "{\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true,\"requestId\":1}";

        ItemCreateRequestDto dto = createRequestJson.parse(json).getObject();

        assertThat(dto.name()).isEqualTo("Test Item");
        assertThat(dto.description()).isEqualTo("Test Description");
        assertThat(dto.available()).isTrue();
        assertThat(dto.requestId()).isEqualTo(1L);
    }

    @Test
    void itemUpdateRequestDto_serialization() throws Exception {
        ItemUpdateRequestDto dto = new ItemUpdateRequestDto("Updated Name", "Updated Description", false);

        String jsonContent = updateRequestJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"name\":\"Updated Name\"");
        assertThat(jsonContent).contains("\"description\":\"Updated Description\"");
        assertThat(jsonContent).contains("\"available\":false");
    }

    @Test
    void itemUpdateRequestDto_deserialization() throws Exception {
        String json = "{\"name\":\"Updated Name\",\"description\":\"Updated Description\",\"available\":false}";

        ItemUpdateRequestDto dto = updateRequestJson.parse(json).getObject();

        assertThat(dto.name()).isEqualTo("Updated Name");
        assertThat(dto.description()).isEqualTo("Updated Description");
        assertThat(dto.available()).isFalse();
    }

    @Test
    void itemResponseDto_serialization() throws Exception {
        ItemResponseDto dto = new ItemResponseDto(
                1L,
                "Test Item",
                "Test Description",
                true,
                new UserResponseDto(1L, "Owner", "owner@example.com"),
                null,
                List.of(),
                null,
                null
        );

        String jsonContent = responseJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"name\":\"Test Item\"");
        assertThat(jsonContent).contains("\"description\":\"Test Description\"");
        assertThat(jsonContent).contains("\"available\":true");
        assertThat(jsonContent).contains("\"owner\"");
        assertThat(jsonContent).contains("\"comments\"");
    }

    @Test
    void itemResponseDto_deserialization() throws Exception {
        String json = "{" +
                "\"id\":1," +
                "\"name\":\"Test Item\"," +
                "\"description\":\"Test Description\"," +
                "\"available\":true," +
                "\"owner\":{\"id\":1,\"name\":\"Owner\",\"email\":\"owner@example.com\"}," +
                "\"requestId\":null," +
                "\"comments\":[]," +
                "\"lastBooking\":null," +
                "\"nextBooking\":null" +
                "}";

        ItemResponseDto dto = responseJson.parse(json).getObject();

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Test Item");
        assertThat(dto.description()).isEqualTo("Test Description");
        assertThat(dto.available()).isTrue();
        assertThat(dto.owner()).isNotNull();
        assertThat(dto.owner().id()).isEqualTo(1L);
        assertThat(dto.comments()).isEmpty();
        assertThat(dto.lastBooking()).isNull();
        assertThat(dto.nextBooking()).isNull();
    }

    @Test
    void commentCreateRequestDto_serialization() throws Exception {
        CommentCreateRequestDto dto = new CommentCreateRequestDto("Great item!");

        String jsonContent = commentCreateJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"text\":\"Great item!\"");
    }

    @Test
    void commentCreateRequestDto_deserialization() throws Exception {
        String json = "{\"text\":\"Great item!\"}";

        CommentCreateRequestDto dto = commentCreateJson.parse(json).getObject();

        assertThat(dto.text()).isEqualTo("Great item!");
    }

    @Test
    void commentResponseDto_serialization() throws Exception {
        LocalDateTime created = LocalDateTime.of(2024, 1, 15, 10, 0);
        CommentResponseDto dto = new CommentResponseDto(1L, "Great item!", "Test User", created);

        String jsonContent = commentResponseJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"text\":\"Great item!\"");
        assertThat(jsonContent).contains("\"authorName\":\"Test User\"");
        assertThat(jsonContent).contains("\"created\":\"2024-01-15T10:00:00\"");
    }

    @Test
    void commentResponseDto_deserialization() throws Exception {
        String json = "{\"id\":1,\"text\":\"Great item!\",\"authorName\":\"Test User\",\"created\":\"2024-01-15T10:00:00\"}";

        CommentResponseDto dto = commentResponseJson.parse(json).getObject();

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.text()).isEqualTo("Great item!");
        assertThat(dto.authorName()).isEqualTo("Test User");
        assertThat(dto.created()).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 0));
    }

    @Test
    void itemCreateRequestDto_withNullFields_shouldSerialize() throws Exception {
        ItemCreateRequestDto dto = new ItemCreateRequestDto(null, null, null, null);

        String jsonContent = createRequestJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"name\":null");
        assertThat(jsonContent).contains("\"description\":null");
        assertThat(jsonContent).contains("\"available\":null");
        assertThat(jsonContent).contains("\"requestId\":null");
    }

    @Test
    void itemBaseDto_serialization() throws Exception {
        ItemBaseDto dto = new ItemBaseDto("Test Item", "Test Description", true, 1L);

        String jsonContent = baseDtoJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"name\":\"Test Item\"");
        assertThat(jsonContent).contains("\"description\":\"Test Description\"");
        assertThat(jsonContent).contains("\"available\":true");
        assertThat(jsonContent).contains("\"requestId\":1");
    }

    @Test
    void itemBaseDto_deserialization() throws Exception {
        String json = "{\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true,\"requestId\":1}";

        ItemBaseDto dto = baseDtoJson.parse(json).getObject();

        assertThat(dto.name()).isEqualTo("Test Item");
        assertThat(dto.description()).isEqualTo("Test Description");
        assertThat(dto.available()).isTrue();
        assertThat(dto.requestId()).isEqualTo(1L);
    }

    @Test
    void bookingForItemDto_serialization() throws Exception {
        BookingForItemDto dto = new BookingForItemDto(1L, 2L);

        String jsonContent = bookingForItemJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"bookerId\":2");
    }

    @Test
    void bookingForItemDto_deserialization() throws Exception {
        String json = "{\"id\":1,\"bookerId\":2}";

        BookingForItemDto dto = bookingForItemJson.parse(json).getObject();

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.bookerId()).isEqualTo(2L);
    }

    @Test
    void itemCreateRequestDto_validation() throws JsonProcessingException {
        ItemCreateRequestDto dto = new ItemCreateRequestDto("", null, null, null);
        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"name\":\"\"");
        assertThat(json).contains("\"description\":null");
    }
}
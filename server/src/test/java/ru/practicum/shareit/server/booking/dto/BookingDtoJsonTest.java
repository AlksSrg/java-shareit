package ru.practicum.shareit.server.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingCreateRequestDto> createRequestJson;

    @Autowired
    private JacksonTester<BookingResponseDto> responseJson;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void bookingCreateRequestDto_serialization() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 16, 12, 0);

        BookingCreateRequestDto dto = new BookingCreateRequestDto(1L, start, end);

        String jsonContent = createRequestJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"itemId\":1");
        assertThat(jsonContent).contains("\"start\":\"2024-01-15T10:00:00\"");
        assertThat(jsonContent).contains("\"end\":\"2024-01-16T12:00:00\"");
    }

    @Test
    void bookingCreateRequestDto_deserialization() throws Exception {
        String json = "{\"itemId\":1,\"start\":\"2024-01-15T10:00:00\",\"end\":\"2024-01-16T12:00:00\"}";

        BookingCreateRequestDto dto = createRequestJson.parse(json).getObject();

        assertThat(dto.itemId()).isEqualTo(1L);
        assertThat(dto.start()).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 0));
        assertThat(dto.end()).isEqualTo(LocalDateTime.of(2024, 1, 16, 12, 0));
    }

    @Test
    void bookingResponseDto_serialization() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 16, 12, 0);

        UserResponseDto booker = new UserResponseDto(200L, "Test User", "test@example.com");

        ItemResponseDto item = new ItemResponseDto(
                100L,
                "Test Item",
                "Test Description",
                true,
                booker,
                null,
                List.of(),
                null,
                null
        );

        BookingResponseDto dto = BookingResponseDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(BookingStatus.WAITING)
                .item(item)
                .booker(booker)
                .build();

        String jsonContent = responseJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"start\":\"2024-01-15T10:00:00\"");
        assertThat(jsonContent).contains("\"end\":\"2024-01-16T12:00:00\"");
        assertThat(jsonContent).contains("\"status\":\"WAITING\"");
        assertThat(jsonContent).contains("\"item\"");
        assertThat(jsonContent).contains("\"booker\"");
    }

    @Test
    void bookingResponseDto_deserialization() throws Exception {
        String json = "{" +
                "\"id\":1," +
                "\"start\":\"2024-01-15T10:00:00\"," +
                "\"end\":\"2024-01-16T12:00:00\"," +
                "\"status\":\"WAITING\"," +
                "\"item\":{" +
                "   \"id\":100," +
                "   \"name\":\"Test Item\"," +
                "   \"description\":\"Test Description\"," +
                "   \"available\":true," +
                "   \"owner\":{\"id\":300,\"name\":\"Owner\",\"email\":\"owner@example.com\"}," +
                "   \"requestId\":null," +
                "   \"comments\":[]," +
                "   \"lastBooking\":null," +
                "   \"nextBooking\":null" +
                "}," +
                "\"booker\":{\"id\":200,\"name\":\"Test User\",\"email\":\"test@example.com\"}" +
                "}";

        BookingResponseDto dto = responseJson.parse(json).getObject();

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.start()).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 0));
        assertThat(dto.end()).isEqualTo(LocalDateTime.of(2024, 1, 16, 12, 0));
        assertThat(dto.status()).isEqualTo(BookingStatus.WAITING);

        assertThat(dto.booker()).isNotNull();
        assertThat(dto.booker().id()).isEqualTo(200L);
        assertThat(dto.booker().name()).isEqualTo("Test User");
        assertThat(dto.booker().email()).isEqualTo("test@example.com");

        assertThat(dto.item()).isNotNull();
        assertThat(dto.item().id()).isEqualTo(100L);
        assertThat(dto.item().name()).isEqualTo("Test Item");
        assertThat(dto.item().description()).isEqualTo("Test Description");
        assertThat(dto.item().available()).isTrue();
        assertThat(dto.item().owner()).isNotNull();
        assertThat(dto.item().owner().id()).isEqualTo(300L);
        assertThat(dto.item().comments()).isEmpty();
        assertThat(dto.item().lastBooking()).isNull();
        assertThat(dto.item().nextBooking()).isNull();
    }

    @Test
    void bookingResponseDto_deserialization_withMinimalData() throws Exception {
        String json = "{" +
                "\"id\":1," +
                "\"start\":\"2024-01-15T10:00:00\"," +
                "\"end\":\"2024-01-16T12:00:00\"," +
                "\"status\":\"WAITING\"" +
                "}";

        BookingResponseDto dto = responseJson.parse(json).getObject();

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.start()).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 0));
        assertThat(dto.end()).isEqualTo(LocalDateTime.of(2024, 1, 16, 12, 0));
        assertThat(dto.status()).isEqualTo(BookingStatus.WAITING);
        assertThat(dto.item()).isNull();
        assertThat(dto.booker()).isNull();
    }

    @Test
    void bookingCreateRequestDto_withNullFields_shouldSerialize() throws Exception {
        BookingCreateRequestDto dto = new BookingCreateRequestDto(null, null, null);

        String jsonContent = createRequestJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"itemId\":null");
        assertThat(jsonContent).contains("\"start\":null");
        assertThat(jsonContent).contains("\"end\":null");
    }

    @Test
    void bookingCreateRequestDto_withNullFields_shouldDeserialize() throws Exception {
        String json = "{\"itemId\":null,\"start\":null,\"end\":null}";

        BookingCreateRequestDto dto = createRequestJson.parse(json).getObject();

        assertThat(dto.itemId()).isNull();
        assertThat(dto.start()).isNull();
        assertThat(dto.end()).isNull();
    }

    @Test
    void bookingResponseDto_serialization_withNullFields() throws Exception {
        BookingResponseDto dto = BookingResponseDto.builder()
                .id(1L)
                .start(null)
                .end(null)
                .status(null)
                .item(null)
                .booker(null)
                .build();

        String jsonContent = responseJson.write(dto).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"start\":null");
        assertThat(jsonContent).contains("\"end\":null");
        assertThat(jsonContent).contains("\"status\":null");
        assertThat(jsonContent).contains("\"item\":null");
        assertThat(jsonContent).contains("\"booker\":null");
    }
}
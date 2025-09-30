package ru.practicum.shareit.server.request.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    private final ItemRequestMapper mapper = Mappers.getMapper(ItemRequestMapper.class);

    @Test
    void toDto_shouldConvertEntityToDto() {
        User requestor = User.builder()
                .id(1L)
                .name("User Name")
                .email("user@example.com")
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Need item description")
                .requestor(requestor)
                .created(LocalDateTime.of(2023, 1, 1, 10, 0))
                .build();

        ItemRequestDto dto = mapper.toDto(itemRequest);

        assertNotNull(dto);
        assertEquals(itemRequest.getId(), dto.id());
        assertEquals(itemRequest.getDescription(), dto.description());
        assertEquals(itemRequest.getCreated(), dto.created());
        assertNull(dto.items());
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        ItemRequestDto dto = new ItemRequestDto(
                1L,
                "Need item description",
                LocalDateTime.of(2023, 1, 1, 10, 0),
                null
        );

        ItemRequest entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.description(), entity.getDescription());
        assertNull(entity.getRequestor());
        assertNull(entity.getCreated());
    }

    @Test
    void toDto_shouldHandleNull() {
        ItemRequestDto dto = mapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toEntity_shouldHandleNull() {
        ItemRequest entity = mapper.toEntity(null);

        assertNull(entity);
    }
}
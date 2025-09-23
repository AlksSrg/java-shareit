package ru.practicum.shareit.server.request.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.user.model.User;

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
        assertEquals(itemRequest.getId(), dto.getId());
        assertEquals(itemRequest.getDescription(), dto.getDescription());
        assertEquals(itemRequest.getCreated(), dto.getCreated());
        assertNull(dto.getItems());
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("Need item description")
                .created(LocalDateTime.of(2023, 1, 1, 10, 0))
                .build();

        ItemRequest entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getDescription(), entity.getDescription());
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
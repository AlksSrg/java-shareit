package ru.practicum.shareit.server.booking.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.server.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    private final BookingMapper mapper = Mappers.getMapper(BookingMapper.class);

    @Test
    void toEntity_shouldMapCreateRequestToEntity() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingCreateRequestDto dto = new BookingCreateRequestDto(1L, start, end);

        Booking entity = mapper.toEntity(dto);

        assertThat(entity.getStart()).isEqualTo(start);
        assertThat(entity.getEnd()).isEqualTo(end);
        assertThat(entity.getItem()).isNull();
        assertThat(entity.getBooker()).isNull();
        assertThat(entity.getStatus()).isNull();
    }

    @Test
    void toResponseDto_shouldMapEntityToResponseDto() {
        Booking entity = new Booking();
        entity.setId(1L);
        entity.setStart(LocalDateTime.now().plusDays(1));
        entity.setEnd(LocalDateTime.now().plusDays(2));
        entity.setStatus(BookingStatus.WAITING);

        var dto = mapper.toResponseDto(entity);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(entity.getStart());
        assertThat(dto.getEnd()).isEqualTo(entity.getEnd());
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }
}
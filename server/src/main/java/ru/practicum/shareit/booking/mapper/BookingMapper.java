package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

/**
 * Маппер для преобразования между DTO и сущностью Booking.
 * Использует MapStruct для автоматического генерации кода преобразования.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    /**
     * Преобразует DTO создания в сущность Booking.
     *
     * @param dto DTO для создания бронирования
     * @return сущность Booking
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "booker", ignore = true)
    @Mapping(target = "status", ignore = true)
    Booking toEntity(BookingCreateRequestDto dto);

    /**
     * Преобразует сущность Booking в DTO ответа.
     *
     * @param booking сущность бронирования
     * @return DTO ответа
     */
    @Mapping(target = "item.comments", ignore = true)
    @Mapping(target = "item.lastBooking", ignore = true)
    @Mapping(target = "item.nextBooking", ignore = true)
    BookingResponseDto toResponseDto(Booking booking);
}
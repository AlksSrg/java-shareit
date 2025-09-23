package ru.practicum.shareit.server.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.booking.BookingNotFoundException;
import ru.practicum.shareit.exception.booking.BookingNotOwnedException;
import ru.practicum.shareit.exception.booking.BookingStatusAlreadySetException;
import ru.practicum.shareit.exception.booking.UnavailableItemException;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.user.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Sql(scripts = {"/test-schema.sql", "/test-data.sql"})
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingServiceImpl bookingService;

    private Long createdBookingId;

    @BeforeEach
    void setUp() {
        createdBookingId = null;
    }

    @Test
    void create_shouldCreateBooking() {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                3L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        BookingResponseDto result = bookingService.create(2L, request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getItem()).isNotNull();
        assertThat(result.getItem().getId()).isEqualTo(3L);
        assertThat(result.getBooker()).isNotNull();
        assertThat(result.getBooker().id()).isEqualTo(2L);
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING);

        createdBookingId = result.getId();
    }

    @Test
    void create_withUnavailableItem_shouldThrowException() {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                2L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(UnavailableItemException.class, () ->
                bookingService.create(2L, request)
        );
    }

    @Test
    void create_withNonExistentUser_shouldThrowException() {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(UserNotFoundException.class, () ->
                bookingService.create(999L, request)
        );
    }

    @Test
    void create_withNonExistentItem_shouldThrowException() {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                999L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(ItemNotFoundException.class, () ->
                bookingService.create(2L, request)
        );
    }

    @Test
    void create_ownerBookingOwnItem_shouldThrowException() {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        assertThrows(BookingNotOwnedException.class, () ->
                bookingService.create(1L, request)
        );
    }

    @Test
    void updateStatus_shouldApproveBooking() {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                3L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto created = bookingService.create(2L, request);

        BookingResponseDto result = bookingService.updateStatus(1L, created.getId(), true);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void updateStatus_shouldRejectBooking() {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                3L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto created = bookingService.create(2L, request);

        BookingResponseDto result = bookingService.updateStatus(1L, created.getId(), false);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BookingStatus.REJECTED);
    }

    @Test
    void updateStatus_withNonOwner_shouldThrowException() {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                3L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto created = bookingService.create(2L, request);

        assertThrows(BookingNotOwnedException.class, () ->
                bookingService.updateStatus(3L, created.getId(), true)
        );
    }

    @Test
    void updateStatus_alreadyApproved_shouldThrowException() {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                3L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto created = bookingService.create(2L, request);

        bookingService.updateStatus(1L, created.getId(), true);

        assertThrows(BookingStatusAlreadySetException.class, () ->
                bookingService.updateStatus(1L, created.getId(), false)
        );
    }

    @Test
    void findById_shouldReturnBooking() {
        BookingResponseDto result = bookingService.findById(1L, 1L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        BookingResponseDto result2 = bookingService.findById(2L, 1L);
        assertThat(result2).isNotNull();
        assertThat(result2.getId()).isEqualTo(1L);
    }

    @Test
    void findById_withNonRelatedUser_shouldThrowException() {
        assertThrows(BookingNotOwnedException.class, () ->
                bookingService.findById(3L, 1L)
        );
    }

    @Test
    void findByBookerId_shouldReturnBookings() {
        List<BookingResponseDto> result = bookingService.findByBookerId(
                2L, BookingStatus.ALL, 0, 10
        );

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(booking -> booking.getBooker().id().equals(2L));
    }

    @Test
    void findByOwnerId_shouldReturnBookings() {
        List<BookingResponseDto> result = bookingService.findByOwnerId(
                1L, BookingStatus.ALL, 0, 10
        );

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(booking -> booking.getItem().getOwner().id().equals(1L));
    }

    @Test
    void getBookingOrThrow_shouldReturnBooking() {
        var result = bookingService.getBookingOrThrow(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getBookingOrThrow_withNonExistentBooking_shouldThrowException() {
        assertThrows(BookingNotFoundException.class, () ->
                bookingService.getBookingOrThrow(999L)
        );
    }

    @Test
    void findByBookerId_withDifferentStatuses_shouldReturnAppropriateResults() {
        List<BookingResponseDto> allResult = bookingService.findByBookerId(
                2L, BookingStatus.ALL, 0, 10
        );
        assertThat(allResult).isNotEmpty();

        List<BookingResponseDto> approvedResult = bookingService.findByBookerId(
                2L, BookingStatus.APPROVED, 0, 10
        );
        assertThat(approvedResult).isNotEmpty();

        List<BookingResponseDto> waitingResult = bookingService.findByBookerId(
                2L, BookingStatus.WAITING, 0, 10
        );
        assertThat(waitingResult).isNotEmpty();

        List<BookingResponseDto> rejectedResult = bookingService.findByBookerId(
                2L, BookingStatus.REJECTED, 0, 10
        );
        assertThat(rejectedResult).isEmpty();
    }

    @Test
    void findByOwnerId_withDifferentStatuses_shouldReturnAppropriateResults() {
        List<BookingResponseDto> allResult = bookingService.findByOwnerId(
                1L, BookingStatus.ALL, 0, 10
        );
        assertThat(allResult).isNotEmpty();

        List<BookingResponseDto> approvedResult = bookingService.findByOwnerId(
                1L, BookingStatus.APPROVED, 0, 10
        );
        assertThat(approvedResult).isNotEmpty();

        List<BookingResponseDto> waitingResult = bookingService.findByOwnerId(
                1L, BookingStatus.WAITING, 0, 10
        );
        assertThat(waitingResult).isNotEmpty();

        List<BookingResponseDto> rejectedResult = bookingService.findByOwnerId(
                1L, BookingStatus.REJECTED, 0, 10
        );
        assertThat(rejectedResult).isNotEmpty();
    }

    @Test
    void findByBookerId_withPagination_shouldReturnLimitedResults() {
        List<BookingResponseDto> result = bookingService.findByBookerId(
                2L, BookingStatus.ALL, 0, 1
        );

        assertThat(result).hasSize(1);
    }

    @Test
    void findByOwnerId_withPagination_shouldReturnLimitedResults() {
        List<BookingResponseDto> result = bookingService.findByOwnerId(
                1L, BookingStatus.ALL, 0, 1
        );

        assertThat(result).hasSize(1);
    }

    @Test
    void create_newBooking_shouldAppearInSearchResults() {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                3L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto created = bookingService.create(2L, request);

        List<BookingResponseDto> waitingResult = bookingService.findByBookerId(
                2L, BookingStatus.WAITING, 0, 10
        );
        assertThat(waitingResult).isNotEmpty();
        assertThat(waitingResult).anyMatch(booking -> booking.getId().equals(created.getId()));

        List<BookingResponseDto> ownerWaitingResult = bookingService.findByOwnerId(
                1L, BookingStatus.WAITING, 0, 10
        );
        assertThat(ownerWaitingResult).isNotEmpty();
        assertThat(ownerWaitingResult).anyMatch(booking -> booking.getId().equals(created.getId()));
    }
}
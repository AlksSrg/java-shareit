package ru.practicum.shareit.server.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private UserResponseDto createTestUserResponse(Long id) {
        return new UserResponseDto(id, "Test User " + id, "user" + id + "@example.com");
    }

    private ItemResponseDto createTestItemResponse(Long id, Long ownerId) {
        return new ItemResponseDto(
                id,
                "Test Item " + id,
                "Test Description " + id,
                true,
                createTestUserResponse(ownerId),
                null,
                Collections.emptyList(),
                null,
                null
        );
    }

    private BookingResponseDto createTestBookingResponse() {
        return BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .item(createTestItemResponse(1L, 1L))
                .booker(createTestUserResponse(2L))
                .build();
    }

    @Test
    void create_shouldReturnBooking() throws Exception {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto response = createTestBookingResponse();

        Mockito.when(bookingService.create(eq(2L), any(BookingCreateRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.item.id").value(1L))
                .andExpect(jsonPath("$.item.name").value("Test Item 1"))
                .andExpect(jsonPath("$.booker.id").value(2L))
                .andExpect(jsonPath("$.booker.name").value("Test User 2"));
    }

    @Test
    void updateStatus_shouldReturnApprovedBooking() throws Exception {
        BookingResponseDto response = createTestBookingResponse();
        BookingResponseDto approvedResponse = BookingResponseDto.builder()
                .id(response.id())
                .start(response.start())
                .end(response.end())
                .status(BookingStatus.APPROVED)
                .item(response.item())
                .booker(response.booker())
                .build();

        Mockito.when(bookingService.updateStatus(eq(1L), eq(1L), eq(true)))
                .thenReturn(approvedResponse);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void updateStatus_shouldReturnRejectedBooking() throws Exception {
        BookingResponseDto response = createTestBookingResponse();
        BookingResponseDto rejectedResponse = BookingResponseDto.builder()
                .id(response.id())
                .start(response.start())
                .end(response.end())
                .status(BookingStatus.REJECTED)
                .item(response.item())
                .booker(response.booker())
                .build();

        Mockito.when(bookingService.updateStatus(eq(1L), eq(1L), eq(false)))
                .thenReturn(rejectedResponse);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    void findById_shouldReturnBooking() throws Exception {
        BookingResponseDto response = createTestBookingResponse();

        Mockito.when(bookingService.findById(eq(1L), eq(1L)))
                .thenReturn(response);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void findByBookerId_withAllStatus_shouldReturnBookings() throws Exception {
        BookingResponseDto response = createTestBookingResponse();

        Mockito.when(bookingService.findByBookerId(eq(2L), eq(BookingStatus.ALL), eq(0), eq(10)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .param("status", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void findByBookerId_withWaitingStatus_shouldReturnBookings() throws Exception {
        BookingResponseDto response = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .item(createTestItemResponse(1L, 1L))
                .booker(createTestUserResponse(2L))
                .build();

        Mockito.when(bookingService.findByBookerId(eq(2L), eq(BookingStatus.WAITING), eq(0), eq(10)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .param("status", "WAITING")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    void findByBookerId_withApprovedStatus_shouldReturnBookings() throws Exception {
        BookingResponseDto response = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.APPROVED)
                .item(createTestItemResponse(1L, 1L))
                .booker(createTestUserResponse(2L))
                .build();

        Mockito.when(bookingService.findByBookerId(eq(2L), eq(BookingStatus.APPROVED), eq(0), eq(10)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .param("status", "APPROVED")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }

    @Test
    void findByBookerId_withRejectedStatus_shouldReturnBookings() throws Exception {
        BookingResponseDto response = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.REJECTED)
                .item(createTestItemResponse(1L, 1L))
                .booker(createTestUserResponse(2L))
                .build();

        Mockito.when(bookingService.findByBookerId(eq(2L), eq(BookingStatus.REJECTED), eq(0), eq(10)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .param("status", "REJECTED")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("REJECTED"));
    }

    @Test
    void findByBookerId_withCanceledStatus_shouldReturnBookings() throws Exception {
        BookingResponseDto response = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.CANCELED)
                .item(createTestItemResponse(1L, 1L))
                .booker(createTestUserResponse(2L))
                .build();

        Mockito.when(bookingService.findByBookerId(eq(2L), eq(BookingStatus.CANCELED), eq(0), eq(10)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .param("status", "CANCELED")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("CANCELED"));
    }

    @Test
    void findByOwnerId_withAllStatus_shouldReturnBookings() throws Exception {
        BookingResponseDto response = createTestBookingResponse();

        Mockito.when(bookingService.findByOwnerId(eq(1L), eq(BookingStatus.ALL), eq(0), eq(10)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("status", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void findByOwnerId_withWaitingStatus_shouldReturnBookings() throws Exception {
        BookingResponseDto response = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .item(createTestItemResponse(1L, 1L))
                .booker(createTestUserResponse(2L))
                .build();

        Mockito.when(bookingService.findByOwnerId(eq(1L), eq(BookingStatus.WAITING), eq(0), eq(10)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("status", "WAITING")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    void findByOwnerId_withApprovedStatus_shouldReturnBookings() throws Exception {
        BookingResponseDto response = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.APPROVED)
                .item(createTestItemResponse(1L, 1L))
                .booker(createTestUserResponse(2L))
                .build();

        Mockito.when(bookingService.findByOwnerId(eq(1L), eq(BookingStatus.APPROVED), eq(0), eq(10)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("status", "APPROVED")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }

    @Test
    void findByOwnerId_withRejectedStatus_shouldReturnBookings() throws Exception {
        BookingResponseDto response = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.REJECTED)
                .item(createTestItemResponse(1L, 1L))
                .booker(createTestUserResponse(2L))
                .build();

        Mockito.when(bookingService.findByOwnerId(eq(1L), eq(BookingStatus.REJECTED), eq(0), eq(10)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("status", "REJECTED")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("REJECTED"));
    }

    @Test
    void findByOwnerId_withCanceledStatus_shouldReturnBookings() throws Exception {
        BookingResponseDto response = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.CANCELED)
                .item(createTestItemResponse(1L, 1L))
                .booker(createTestUserResponse(2L))
                .build();

        Mockito.when(bookingService.findByOwnerId(eq(1L), eq(BookingStatus.CANCELED), eq(0), eq(10)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("status", "CANCELED")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("CANCELED"));
    }

    @Test
    void create_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        BookingCreateRequestDto request = new BookingCreateRequestDto(
                1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)
        );

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Отсутствует обязательный заголовок: X-Sharer-User-Id"));
    }

    @Test
    void updateStatus_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Отсутствует обязательный заголовок: X-Sharer-User-Id"));
    }

    @Test
    void findById_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/bookings/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Отсутствует обязательный заголовок: X-Sharer-User-Id"));
    }

    @Test
    void findByBookerId_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .param("status", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Отсутствует обязательный заголовок: X-Sharer-User-Id"));
    }

    @Test
    void findByOwnerId_withoutUserIdHeader_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .param("status", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Отсутствует обязательный заголовок: X-Sharer-User-Id"));
    }

    @Test
    void findByBookerId_withPagination_shouldReturnBookings() throws Exception {
        BookingResponseDto response = createTestBookingResponse();

        Mockito.when(bookingService.findByBookerId(eq(2L), eq(BookingStatus.ALL), eq(5), eq(20)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 2L)
                        .param("status", "ALL")
                        .param("from", "5")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void findByOwnerId_withPagination_shouldReturnBookings() throws Exception {
        BookingResponseDto response = createTestBookingResponse();

        Mockito.when(bookingService.findByOwnerId(eq(1L), eq(BookingStatus.ALL), eq(10), eq(5)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("status", "ALL")
                        .param("from", "10")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
package ru.practicum.shareit.gateway.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.gateway.booking.client.BookingClient;
import ru.practicum.shareit.gateway.booking.dto.BookingCreateRequestDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    private final BookingCreateRequestDto validBookingRequest = new BookingCreateRequestDto(
            1L,
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusDays(1)
    );

    private final BookingCreateRequestDto invalidBookingRequest = new BookingCreateRequestDto(
            null,
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now().minusDays(1)
    );

    @Test
    void create_WithValidData_ShouldReturnOk() throws Exception {
        when(bookingClient.create(anyLong(), any(BookingCreateRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void create_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBookingRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WithoutUserIdHeader_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStatus_ShouldReturnOk() throws Exception {
        when(bookingClient.updateStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void findById_ShouldReturnOk() throws Exception {
        when(bookingClient.findById(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void findByBookerId_WithDefaultParameters_ShouldReturnOk() throws Exception {
        when(bookingClient.findByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void findByBookerId_WithCustomParameters_ShouldReturnOk() throws Exception {
        when(bookingClient.findByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "WAITING")
                        .param("from", "5")
                        .param("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void findByOwnerId_WithDefaultParameters_ShouldReturnOk() throws Exception {
        when(bookingClient.findByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void findByOwnerId_WithCustomParameters_ShouldReturnOk() throws Exception {
        when(bookingClient.findByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "APPROVED")
                        .param("from", "10")
                        .param("size", "50"))
                .andExpect(status().isOk());
    }

    @Test
    void findByBookerId_WithInvalidPagination_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "-1")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByOwnerId_WithInvalidPagination_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "-5")
                        .param("size", "-10"))
                .andExpect(status().isBadRequest());
    }
}
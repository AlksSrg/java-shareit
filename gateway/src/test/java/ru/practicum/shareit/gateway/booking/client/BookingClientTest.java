package ru.practicum.shareit.gateway.booking.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingClientTest {

    @Mock
    private RestTemplate restTemplate;

    private BookingClient bookingClient;

    private final Long userId = 1L;
    private final Long bookingId = 1L;
    private final BookingCreateRequestDto bookingCreateRequestDto = new BookingCreateRequestDto(
            1L,
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusDays(1)
    );

    @BeforeEach
    void setUp() {
        bookingClient = new BookingClient(restTemplate);
    }

    @Test
    void create_ShouldCallPostMethod() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(new Object()));

        Object response = bookingClient.create(userId, bookingCreateRequestDto);

        assertNotNull(response);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(
                urlCaptor.capture(),
                eq(HttpMethod.POST),
                any(),
                eq(Object.class)
        );

        String actualUrl = urlCaptor.getValue();
        assertTrue(actualUrl.contains("/bookings"));
    }

    @Test
    void updateStatus_ShouldCallPatchMethod() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PATCH),
                any(),
                eq(Object.class),
                anyMap()
        )).thenReturn(ResponseEntity.ok(new Object()));

        Object response = bookingClient.updateStatus(userId, bookingId, true);

        assertNotNull(response);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(
                urlCaptor.capture(),
                eq(HttpMethod.PATCH),
                any(),
                eq(Object.class),
                eq(Map.of("approved", true))
        );

        String actualUrl = urlCaptor.getValue();
        assertTrue(actualUrl.contains("/bookings/1"));
        assertTrue(actualUrl.contains("approved={approved}"));
    }

    @Test
    void findById_ShouldCallGetMethod() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(new Object()));

        Object response = bookingClient.findById(userId, bookingId);

        assertNotNull(response);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(
                urlCaptor.capture(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)
        );

        String actualUrl = urlCaptor.getValue();
        assertTrue(actualUrl.contains("/bookings/1"));
    }

    @Test
    void findByBookerId_ShouldCallGetMethodWithParameters() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class),
                anyMap()
        )).thenReturn(ResponseEntity.ok(new Object()));

        Object response = bookingClient.findByBookerId(userId, "ALL", 0, 10);

        assertNotNull(response);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(
                urlCaptor.capture(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class),
                eq(Map.of("state", "ALL", "from", 0, "size", 10))
        );

        String actualUrl = urlCaptor.getValue();
        assertTrue(actualUrl.contains("/bookings"));
        assertTrue(actualUrl.contains("state={state}"));
        assertTrue(actualUrl.contains("from={from}"));
        assertTrue(actualUrl.contains("size={size}"));
    }

    @Test
    void findByOwnerId_ShouldCallGetMethodWithParameters() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class),
                anyMap()
        )).thenReturn(ResponseEntity.ok(new Object()));

        Object response = bookingClient.findByOwnerId(userId, "ALL", 0, 10);

        assertNotNull(response);

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(
                urlCaptor.capture(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class),
                eq(Map.of("state", "ALL", "from", 0, "size", 10))
        );

        String actualUrl = urlCaptor.getValue();
        assertTrue(actualUrl.contains("/bookings/owner"));
        assertTrue(actualUrl.contains("state={state}"));
        assertTrue(actualUrl.contains("from={from}"));
        assertTrue(actualUrl.contains("size={size}"));
    }
}
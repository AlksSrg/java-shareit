package ru.practicum.shareit.gateway.item.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemClientTest {

    @Mock
    private RestTemplate restTemplate;

    private ItemClient itemClient;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        itemClient = new ItemClient(restTemplate);

        Field serverUrlField = BaseClient.class.getDeclaredField("serverUrl");
        serverUrlField.setAccessible(true);
        serverUrlField.set(itemClient, "http://localhost:9090");
    }

    @Test
    void create_ShouldCallPostWithCorrectParameters() {
        Long ownerId = 1L;
        ItemCreateRequestDto request = new ItemCreateRequestDto("Item", "Description", true, null);
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(restTemplate.exchange(
                eq("http://localhost:9090/items"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.create(ownerId, request);

        assertNotNull(response);
        verify(restTemplate).exchange(
                eq("http://localhost:9090/items"),
                eq(HttpMethod.POST),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return headers.containsKey("X-Sharer-User-Id") &&
                            headers.getFirst("X-Sharer-User-Id").equals("1") &&
                            entity.getBody().equals(request);
                }),
                eq(Object.class)
        );
    }

    @Test
    void update_ShouldCallPatchWithCorrectParameters() {
        Long itemId = 1L;
        Long ownerId = 1L;
        ItemUpdateRequestDto request = new ItemUpdateRequestDto("Updated", "New description", true);
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(restTemplate.exchange(
                eq("http://localhost:9090/items/1"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.update(itemId, ownerId, request);

        assertNotNull(response);
        verify(restTemplate).exchange(
                eq("http://localhost:9090/items/1"),
                eq(HttpMethod.PATCH),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return headers.containsKey("X-Sharer-User-Id") &&
                            headers.getFirst("X-Sharer-User-Id").equals("1") &&
                            entity.getBody().equals(request);
                }),
                eq(Object.class)
        );
    }

    @Test
    void delete_ShouldCallDeleteWithCorrectParameters() {
        Long itemId = 1L;
        Long ownerId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(restTemplate.exchange(
                eq("http://localhost:9090/items/1"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.delete(itemId, ownerId);

        assertNotNull(response);
        verify(restTemplate).exchange(
                eq("http://localhost:9090/items/1"),
                eq(HttpMethod.DELETE),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return headers.containsKey("X-Sharer-User-Id") &&
                            headers.getFirst("X-Sharer-User-Id").equals("1");
                }),
                eq(Object.class)
        );
    }

    @Test
    void findById_ShouldCallGetWithCorrectParameters() {
        Long itemId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(restTemplate.exchange(
                eq("http://localhost:9090/items/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.findById(itemId);

        assertNotNull(response);
        verify(restTemplate).exchange(
                eq("http://localhost:9090/items/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void findByOwnerId_ShouldCallGetWithPaginationParameters() {
        Long ownerId = 1L;
        Integer from = 0;
        Integer size = 10;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(restTemplate.exchange(
                eq("http://localhost:9090/items?from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("from", 0, "size", 10))
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.findByOwnerId(ownerId, from, size);

        assertNotNull(response);
        verify(restTemplate).exchange(
                eq("http://localhost:9090/items?from={from}&size={size}"),
                eq(HttpMethod.GET),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return headers.containsKey("X-Sharer-User-Id") &&
                            headers.getFirst("X-Sharer-User-Id").equals("1");
                }),
                eq(Object.class),
                eq(Map.of("from", 0, "size", 10))
        );
    }

    @Test
    void searchAvailableItems_ShouldCallGetWithSearchParameters() {
        String text = "test";
        Integer from = 0;
        Integer size = 10;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(restTemplate.exchange(
                eq("http://localhost:9090/items/search?text={text}&from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("text", "test", "from", 0, "size", 10))
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.searchAvailableItems(text, from, size);

        assertNotNull(response);
        verify(restTemplate).exchange(
                eq("http://localhost:9090/items/search?text={text}&from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("text", "test", "from", 0, "size", 10))
        );
    }

    @Test
    void addComment_ShouldCallPostWithCommentParameters() {
        Long itemId = 1L;
        Long userId = 1L;
        CommentCreateRequestDto request = new CommentCreateRequestDto("Great item!");
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();

        when(restTemplate.exchange(
                eq("http://localhost:9090/items/1/comment"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemClient.addComment(itemId, userId, request);

        assertNotNull(response);
        verify(restTemplate).exchange(
                eq("http://localhost:9090/items/1/comment"),
                eq(HttpMethod.POST),
                argThat(entity -> {
                    HttpHeaders headers = entity.getHeaders();
                    return headers.containsKey("X-Sharer-User-Id") &&
                            headers.getFirst("X-Sharer-User-Id").equals("1") &&
                            entity.getBody().equals(request);
                }),
                eq(Object.class)
        );
    }
}
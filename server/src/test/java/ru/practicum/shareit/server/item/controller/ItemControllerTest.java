package ru.practicum.shareit.server.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.item.ItemNotOwnedByUserException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void createItem_shouldReturnCreatedItem() throws Exception {
        Long ownerId = 1L;
        ItemCreateRequestDto createDto = new ItemCreateRequestDto(
                "Test Item", "Test Description", true, null);
        ItemResponseDto responseDto = createItemResponseDto(1L, ownerId);

        when(itemService.create(eq(ownerId), any(ItemCreateRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Item")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void createItem_shouldReturnBadRequestWhenMissingHeader() throws Exception {
        ItemCreateRequestDto createDto = new ItemCreateRequestDto(
                "Test Item", "Test Description", true, null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        Long itemId = 1L;
        Long ownerId = 1L;
        ItemUpdateRequestDto updateDto = new ItemUpdateRequestDto(
                "Updated Name", "Updated Description", false);
        ItemResponseDto responseDto = createItemResponseDto(itemId, ownerId);

        when(itemService.update(eq(itemId), eq(ownerId), any(ItemUpdateRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId.intValue())))
                .andExpect(jsonPath("$.name", is("Test Item")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void updateItem_shouldReturnNotFoundWhenItemNotExists() throws Exception {
        Long itemId = 999L;
        Long ownerId = 1L;
        ItemUpdateRequestDto updateDto = new ItemUpdateRequestDto("New Name", null, null);

        when(itemService.update(eq(itemId), eq(ownerId), any(ItemUpdateRequestDto.class)))
                .thenThrow(new ItemNotFoundException("Item not found"));

        mockMvc.perform(patch("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getItem_shouldReturnItem() throws Exception {
        Long itemId = 1L;
        ItemResponseDto responseDto = createItemResponseDto(itemId, 1L);

        when(itemService.findById(itemId)).thenReturn(responseDto);

        mockMvc.perform(get("/items/{id}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId.intValue())))
                .andExpect(jsonPath("$.name", is("Test Item")));
    }

    @Test
    void getItem_shouldReturnNotFoundWhenItemNotExists() throws Exception {
        Long itemId = 999L;

        when(itemService.findById(itemId)).thenThrow(new ItemNotFoundException("Item not found"));

        mockMvc.perform(get("/items/{id}", itemId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersItems_shouldReturnUserItems() throws Exception {
        Long ownerId = 1L;
        List<ItemResponseDto> items = List.of(
                createItemResponseDto(1L, ownerId),
                createItemResponseDto(2L, ownerId)
        );

        when(itemService.findByOwnerId(eq(ownerId), anyInt(), anyInt()))
                .thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", ownerId)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void getUsersItems_shouldUseDefaultPagination() throws Exception {
        Long ownerId = 1L;
        List<ItemResponseDto> items = List.of(createItemResponseDto(1L, ownerId));

        when(itemService.findByOwnerId(eq(ownerId), eq(0), eq(10)))
                .thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void searchItems_shouldReturnFoundItems() throws Exception {
        String searchText = "test";
        List<ItemResponseDto> items = List.of(createItemResponseDto(1L, 1L));

        when(itemService.searchAvailableItems(eq(searchText), anyInt(), anyInt()))
                .thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .param("text", searchText)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void searchItems_shouldReturnEmptyListForEmptyText() throws Exception {
        String searchText = "";

        when(itemService.searchAvailableItems(eq(searchText), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", searchText)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void addComment_shouldReturnCreatedComment() throws Exception {
        Long itemId = 1L;
        Long userId = 2L;
        CommentCreateRequestDto createDto = new CommentCreateRequestDto("Great item!");
        CommentResponseDto commentDto = new CommentResponseDto(
                1L, "Great item!", "Test User", null);

        when(itemService.addComment(eq(itemId), eq(userId), any(CommentCreateRequestDto.class)))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Great item!")))
                .andExpect(jsonPath("$.authorName", is("Test User")));
    }

    @Test
    void deleteItem_shouldReturnOk() throws Exception {
        Long itemId = 1L;
        Long ownerId = 1L;

        doNothing().when(itemService).delete(itemId, ownerId);

        mockMvc.perform(delete("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", ownerId))
                .andExpect(status().isOk());

        verify(itemService, times(1)).delete(itemId, ownerId);
    }

    @Test
    void deleteItem_shouldReturnNotFoundWhenItemNotOwned() throws Exception {
        Long itemId = 1L;
        Long ownerId = 2L;

        doThrow(new ItemNotOwnedByUserException("Not owned"))
                .when(itemService).delete(itemId, ownerId);

        mockMvc.perform(delete("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", ownerId))
                .andExpect(status().isNotFound());
    }

    private ItemResponseDto createItemResponseDto(Long id, Long ownerId) {
        return new ItemResponseDto(
                id,
                "Test Item",
                "Test Description",
                true,
                new UserResponseDto(ownerId, "Owner", "owner@example.com"),
                null,
                List.of(),
                null,
                null
        );
    }
}
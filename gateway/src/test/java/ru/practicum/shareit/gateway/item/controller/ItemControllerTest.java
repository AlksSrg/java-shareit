package ru.practicum.shareit.gateway.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.GlobalExceptionHandler;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ItemClient itemClient;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createItem_ShouldReturnOk_WhenValidRequest() throws Exception {
        ItemCreateRequestDto request = new ItemCreateRequestDto("Item", "Description", true, null);
        when(itemClient.create(anyLong(), any(ItemCreateRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(itemClient).create(eq(1L), any(ItemCreateRequestDto.class));
    }

    @Test
    void createItem_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        ItemCreateRequestDto invalidRequest = new ItemCreateRequestDto("", "", null, null);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_ShouldReturnOk_WhenValidRequest() throws Exception {
        ItemUpdateRequestDto request = new ItemUpdateRequestDto("Updated", "New description", true);
        when(itemClient.update(anyLong(), anyLong(), any(ItemUpdateRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(itemClient).update(eq(1L), eq(1L), any(ItemUpdateRequestDto.class));
    }

    @Test
    void deleteItem_ShouldReturnOk() throws Exception {
        when(itemClient.delete(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemClient).delete(eq(1L), eq(1L));
    }

    @Test
    void getItem_ShouldReturnOk() throws Exception {
        when(itemClient.findById(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk());

        verify(itemClient).findById(eq(1L));
    }

    @Test
    void getUsersItems_ShouldReturnOk_WithPagination() throws Exception {
        when(itemClient.findByOwnerId(anyLong(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(itemClient).findByOwnerId(eq(1L), eq(0), eq(10));
    }

    @Test
    void getUsersItems_ShouldUseDefaultPagination_WhenNoParams() throws Exception {
        when(itemClient.findByOwnerId(anyLong(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemClient).findByOwnerId(eq(1L), eq(0), eq(10));
    }

    @Test
    void searchItems_ShouldReturnOk_WithValidText() throws Exception {
        when(itemClient.searchAvailableItems(anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items/search")
                        .param("text", "test")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(itemClient).searchAvailableItems(eq("test"), eq(0), eq(10));
    }

    @Test
    void searchItems_ShouldReturnEmptyArray_WhenEmptyText() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", " ")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(itemClient, never()).searchAvailableItems(anyString(), anyInt(), anyInt());
    }

    @Test
    void addComment_ShouldReturnOk_WhenValidRequest() throws Exception {
        CommentCreateRequestDto request = new CommentCreateRequestDto("Great item!");
        when(itemClient.addComment(anyLong(), anyLong(), any(CommentCreateRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(itemClient).addComment(eq(1L), eq(1L), any(CommentCreateRequestDto.class));
    }

    @Test
    void addComment_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        CommentCreateRequestDto invalidRequest = new CommentCreateRequestDto("");

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItem_ShouldReturnBadRequest_WhenMissingUserIdHeader() throws Exception {
        ItemCreateRequestDto request = new ItemCreateRequestDto("Item", "Description", true, null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
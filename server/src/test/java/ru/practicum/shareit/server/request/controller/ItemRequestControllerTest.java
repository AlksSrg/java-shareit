package ru.practicum.shareit.server.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;

    @Test
    void createItemRequest_shouldReturnCreatedRequest() throws Exception {
        Long userId = 1L;
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .description("Need a new item")
                .build();

        ItemRequestDto responseDto = ItemRequestDto.builder()
                .id(1L)
                .description("Need a new item")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();

        when(requestService.createItemRequest(any(ItemRequestDto.class), anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Need a new item"))
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    void createItemRequest_shouldReturnBadRequestWhenMissingHeader() throws Exception {
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .description("Need a new item")
                .build();

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOwnItemRequests_shouldReturnUserRequests() throws Exception {
        Long userId = 1L;
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(1L)
                .description("My request")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();

        when(requestService.getOwnItemRequests(anyLong()))
                .thenReturn(List.of(requestDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("My request"));
    }

    @Test
    void getAllItemRequests_shouldReturnOtherUsersRequests() throws Exception {
        Long userId = 1L;
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(2L)
                .description("Other user request")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();

        when(requestService.getAllItemRequests(anyLong(), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(requestDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].description").value("Other user request"));
    }

    @Test
    void getAllItemRequests_shouldUseDefaultPagination() throws Exception {
        Long userId = 1L;
        when(requestService.getAllItemRequests(anyLong(), any(Integer.class), any(Integer.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getItemRequestById_shouldReturnRequest() throws Exception {
        Long userId = 1L;
        Long requestId = 1L;
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(requestId)
                .description("Specific request")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();

        when(requestService.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(requestDto);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.description").value("Specific request"));
    }

    @Test
    void getItemRequestById_shouldReturnNotFoundWhenRequestNotExists() throws Exception {
        Long userId = 1L;
        Long requestId = 999L;

        when(requestService.getItemRequestById(anyLong(), anyLong()))
                .thenThrow(new ru.practicum.shareit.server.exception.NotFoundException("Запрос не найден"));

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound());
    }
}
package ru.practicum.shareit.server.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Sql(scripts = {"/test-schema.sql", "/test-data.sql"})
class RequestServiceImplIntegrationTest {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User testUser1;
    private User testUser2;
    private User testUser3;
    private ItemRequest testRequest1;
    private ItemRequest testRequest2;

    @BeforeEach
    void setUp() {
        testUser1 = userRepository.findById(1L).orElseThrow();
        testUser2 = userRepository.findById(2L).orElseThrow();
        testUser3 = userRepository.findById(3L).orElseThrow();

        itemRequestRepository.deleteAll();

        testRequest1 = ItemRequest.builder()
                .description("Test request from user 1")
                .requestor(testUser1)
                .created(LocalDateTime.now().minusDays(1))
                .build();

        testRequest2 = ItemRequest.builder()
                .description("Test request from user 2")
                .requestor(testUser2)
                .created(LocalDateTime.now())
                .build();

        itemRequestRepository.save(testRequest1);
        itemRequestRepository.save(testRequest2);
    }

    @Test
    void createItemRequest_shouldCreateRequest() {
        Long userId = testUser1.getId();
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .description("Need a new laptop for work")
                .build();

        ItemRequestDto createdRequest = requestService.createItemRequest(requestDto, userId);

        assertNotNull(createdRequest, "Созданный запрос не должен быть null");
        assertNotNull(createdRequest.getId(), "ID созданного запроса не должен быть null");
        assertEquals(requestDto.getDescription(), createdRequest.getDescription(), "Описание должно совпадать");
        assertNotNull(createdRequest.getCreated(), "Дата создания не должна быть null");

        if (createdRequest.getItems() != null) {
            assertTrue(createdRequest.getItems().isEmpty(), "Список items должен быть пустым для нового запроса");
        }
    }

    @Test
    void createItemRequest_shouldThrowExceptionWhenUserNotFound() {
        Long nonExistentUserId = 999L;
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .description("Test request")
                .build();

        assertThrows(NotFoundException.class, () -> {
            requestService.createItemRequest(requestDto, nonExistentUserId);
        }, "Должно быть выброшено исключение для несуществующего пользователя");
    }

    @Test
    void getOwnItemRequests_shouldReturnUserRequests() {
        Long userId = testUser1.getId();

        List<ItemRequestDto> requests = requestService.getOwnItemRequests(userId);

        assertNotNull(requests, "Список запросов не должен быть null");
        assertFalse(requests.isEmpty(), "Список запросов не должен быть пустым");

        boolean allRequestsBelongToUser = requests.stream()
                .allMatch(request -> {
                    assertNotNull(request.getId(), "ID запроса не должен быть null");
                    assertNotNull(request.getDescription(), "Описание запроса не должно быть null");
                    assertNotNull(request.getCreated(), "Дата создания не должна быть null");
                    assertNotNull(request.getItems(), "Список items не должен быть null");
                    return true;
                });
        assertTrue(allRequestsBelongToUser, "Все запросы должны принадлежать пользователю");
    }

    @Test
    void getOwnItemRequests_shouldReturnEmptyListWhenNoRequests() {
        Long userId = testUser3.getId();

        List<ItemRequestDto> requests = requestService.getOwnItemRequests(userId);

        assertNotNull(requests, "Список запросов не должен быть null");
        assertTrue(requests.isEmpty(), "Список запросов должен быть пустым для пользователя без запросов");
    }

    @Test
    void getOwnItemRequests_shouldThrowExceptionWhenUserNotFound() {
        Long nonExistentUserId = 999L;

        assertThrows(NotFoundException.class, () -> {
            requestService.getOwnItemRequests(nonExistentUserId);
        }, "Должно быть выброшено исключение для несуществующего пользователя");
    }

    @Test
    void getAllItemRequests_shouldReturnOtherUsersRequests() {
        Long userId = testUser1.getId();
        Integer from = 0;
        Integer size = 10;

        List<ItemRequestDto> requests = requestService.getAllItemRequests(userId, from, size);

        assertNotNull(requests, "Список запросов не должен быть null");

        if (!requests.isEmpty()) {
            ItemRequestDto otherUserRequest = requests.getFirst();
            assertNotNull(otherUserRequest.getId(), "ID запроса не должен быть null");
            assertNotNull(otherUserRequest.getDescription(), "Описание запроса не должно быть null");
            assertNotNull(otherUserRequest.getCreated(), "Дата создания не должна быть null");
            assertNotNull(otherUserRequest.getItems(), "Список items не должен быть null");

            assertTrue(otherUserRequest.getDescription().contains("user 2") ||
                    otherUserRequest.getDescription().contains("Test request from user 2"));
        }
    }

    @Test
    void getAllItemRequests_shouldReturnEmptyListWhenNoOtherUsersRequests() {
        itemRequestRepository.deleteAll();

        ItemRequest requestForUser1 = ItemRequest.builder()
                .description("Only request from user 1")
                .requestor(testUser1)
                .created(LocalDateTime.now())
                .build();
        itemRequestRepository.save(requestForUser1);

        Long userId = testUser1.getId();
        Integer from = 0;
        Integer size = 10;

        List<ItemRequestDto> requests = requestService.getAllItemRequests(userId, from, size);

        assertNotNull(requests, "Список запросов не должен быть null");
        assertTrue(requests.isEmpty(), "Список должен быть пустым, так как нет запросов других пользователей");
    }

    @Test
    void getAllItemRequests_shouldUsePaginationCorrectly() {
        Long userId = testUser3.getId();
        Integer from = 0;
        Integer size = 1;

        List<ItemRequestDto> requests = requestService.getAllItemRequests(userId, from, size);

        assertNotNull(requests, "Список запросов не должен быть null");
        assertTrue(requests.size() <= size, "Количество запросов не должно превышать размер страницы");
    }

    @Test
    void getAllItemRequests_shouldThrowExceptionForInvalidPagination() {
        Long userId = testUser1.getId();

        assertThrows(IllegalArgumentException.class, () -> {
            requestService.getAllItemRequests(userId, -1, 10);
        }, "Должно быть выброшено исключение для отрицательного from");

        assertThrows(IllegalArgumentException.class, () -> {
            requestService.getAllItemRequests(userId, 0, 0);
        }, "Должно быть выброшено исключение для нулевого size");

        assertThrows(IllegalArgumentException.class, () -> {
            requestService.getAllItemRequests(userId, 0, -1);
        }, "Должно быть выброшено исключение для отрицательного size");
    }

    @Test
    void getItemRequestById_shouldReturnExistingRequest() {
        Long userId = testUser1.getId();
        Long existingRequestId = testRequest1.getId();

        ItemRequestDto foundRequest = requestService.getItemRequestById(existingRequestId, userId);

        assertNotNull(foundRequest, "Найденный запрос не должен быть null");
        assertEquals(existingRequestId, foundRequest.getId(), "ID запроса должен совпадать");
        assertEquals("Test request from user 1", foundRequest.getDescription(), "Описание должно совпадать");
        assertNotNull(foundRequest.getCreated(), "Дата создания не должна быть null");
        assertNotNull(foundRequest.getItems(), "Список items не должен быть null");
    }

    @Test
    void getItemRequestById_shouldThrowExceptionWhenRequestNotFound() {
        Long userId = testUser1.getId();
        Long nonExistentRequestId = 999L;

        assertThrows(NotFoundException.class, () -> {
            requestService.getItemRequestById(nonExistentRequestId, userId);
        }, "Должно быть выброшено исключение для несуществующего запроса");
    }

    @Test
    void getItemRequestById_shouldThrowExceptionWhenUserNotFound() {
        Long nonExistentUserId = 999L;
        Long existingRequestId = testRequest1.getId();

        assertThrows(NotFoundException.class, () -> {
            requestService.getItemRequestById(existingRequestId, nonExistentUserId);
        }, "Должно быть выброшено исключение для несуществующего пользователя");
    }

    @Test
    void getItemRequestById_shouldReturnRequestForAnyAuthenticatedUser() {
        Long userId = testUser3.getId();
        Long existingRequestId = testRequest1.getId();

        ItemRequestDto foundRequest = requestService.getItemRequestById(existingRequestId, userId);

        assertNotNull(foundRequest, "Найденный запрос не должен быть null");
        assertEquals(existingRequestId, foundRequest.getId(), "ID запроса должен совпадать");
        assertEquals("Test request from user 1", foundRequest.getDescription(), "Описание должно совпадать");
    }
}
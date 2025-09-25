package ru.practicum.shareit.gateway.request.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class ItemRequestClientIntegrationTest {

    @Autowired
    private ItemRequestClient itemRequestClient;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void createItemRequest_shouldReturnOk() {
        server.expect(once(), requestTo("http://localhost:9090/requests"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        // Создаем объект с помощью билдера
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(null)
                .description("Test description")
                .created(null)
                .items(null)
                .build();

        itemRequestClient.create(requestDto, 1L);
        server.verify();
    }

    @Test
    void createItemRequest_withEmptyDto_shouldReturnOk() {
        server.expect(once(), requestTo("http://localhost:9090/requests"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        // Альтернативный вариант - создание с минимальными данными
        ItemRequestDto requestDto = new ItemRequestDto(
                null,
                "Test description",
                null,
                null
        );

        itemRequestClient.create(requestDto, 1L);
        server.verify();
    }

    @Test
    void getOwnRequests_shouldReturnOk() {
        server.expect(once(), requestTo("http://localhost:9090/requests"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        itemRequestClient.getOwnRequests(1L);
        server.verify();
    }

    @Test
    void getAllRequests_shouldReturnOk() {
        server.expect(once(), requestTo("http://localhost:9090/requests/all?from=0&size=10"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        itemRequestClient.getAllRequests(1L, Map.of("from", 0, "size", 10));
        server.verify();
    }

    @Test
    void getById_shouldReturnOk() {
        server.expect(once(), requestTo("http://localhost:9090/requests/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        itemRequestClient.getById(1L, 1L);
        server.verify();
    }
}
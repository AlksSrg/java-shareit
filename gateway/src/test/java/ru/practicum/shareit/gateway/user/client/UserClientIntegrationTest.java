package ru.practicum.shareit.gateway.user.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserCreateRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class UserClientIntegrationTest {

    @Autowired
    private UserClient userClient;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void create_shouldSendPostRequest() {
        server.expect(once(), requestTo("http://localhost:9090/users"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType("application/json"))
                .andRespond(withSuccess());

        UserCreateRequestDto request = new UserCreateRequestDto("Test User", "test@email.com");
        userClient.create(request);

        server.verify();
    }

    @Test
    void update_shouldSendPatchRequest() {
        server.expect(once(), requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(content().contentType("application/json"))
                .andRespond(withSuccess());

        UserUpdateRequestDto request = new UserUpdateRequestDto("Updated User", "updated@email.com");
        userClient.update(1L, request);

        server.verify();
    }

    @Test
    void delete_shouldSendDeleteRequest() {
        server.expect(once(), requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        userClient.delete(1L);

        server.verify();
    }

    @Test
    void findById_shouldSendGetRequest() {
        server.expect(once(), requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        userClient.findById(1L);

        server.verify();
    }

    @Test
    void findAll_shouldSendGetRequest() {
        server.expect(once(), requestTo("http://localhost:9090/users"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        userClient.findAll();

        server.verify();
    }
}
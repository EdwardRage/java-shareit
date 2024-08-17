package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestIncomingDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
public class RequestControllerTest {

    @MockBean
    private RequestService requestService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    private User user;
    private RequestIncomingDto requestIn;
    private Request request;
    private RequestResponseDto requestResp;

    @BeforeEach
    void setUpEntity() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@test.tu");

        requestIn = new RequestIncomingDto();
        requestIn.setDescription("request description test");

        request = new Request();
        request.setId(1L);
        request.setRequestor(user);
        request.setDescription("request description test");
        request.setCreated(LocalDateTime.now());

        requestResp = new RequestResponseDto();
        requestResp.setId(1L);
        requestResp.setDescription("request description test");
        requestResp.setCreated(LocalDateTime.now());
    }

    @Test
    void createRequestTest() throws Exception {
        Mockito.when(requestService.create(requestIn, user.getId())).thenReturn(requestResp);

        mvc.perform(post("/requests")
                    .content(mapper.writeValueAsString(requestResp))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestResp.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestResp.getDescription())));
    }
}

package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    private User user1;
    private User user2;

    @BeforeEach
    void setUpUsers() {

        user2 = new User();
        //user2.setId(2L);
        user2.setName("Test second");
        user2.setEmail("test@second.com");
    }

    @Test
    void getUsersTests() throws Exception {

        List<User> users = Arrays.asList(user1, user2);

        when(userService.get()).thenReturn(users);

        mvc.perform(get("/users")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(users)));
    }

    @Test
    void getUserByIdTest() throws Exception {
        user2 = new User();
        user2.setId(2L);
        user2.setName("Test second");
        user2.setEmail("test@second.com");
        when(userService.getUserById(2L)).thenReturn(user2);

        mvc.perform(get("/users/2")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user2.getName())))
                .andExpect(jsonPath("$.email", is(user2.getEmail())));
    }

    @Test
    void createUsersTest() throws Exception {
        user1 = new User();
        //user1.setId(0);
        user1.setName("Test");
        user1.setEmail("test@test.com");
        User newUser = new User();
        newUser.setId(1);
        newUser.setName("Test");
        newUser.setEmail("test@test.com");

        when(userService.create(any())).thenReturn(newUser);

        mvc.perform(post("/users")
                    .content(mapper.writeValueAsString(newUser))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newUser.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(newUser.getName())))
                .andExpect(jsonPath("$.email", is(newUser.getEmail())));
    }
}
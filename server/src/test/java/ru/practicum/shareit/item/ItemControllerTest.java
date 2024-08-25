package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    private User user = new User();
    private Item itemTest = new Item();
    private ItemRequestDto itemRequestDto = new ItemRequestDto();
    private ItemResponseDto itemResponseDto = new ItemResponseDto();

    @BeforeEach
    void setUpEntity() {
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@test.tu");


        itemTest.setId(1L);
        itemTest.setName("itemTest");
        itemTest.setDescription("Test description");
        itemTest.setAvailable(true);

        itemRequestDto.setName("itemTest");
        itemRequestDto.setDescription("Test description");
        itemRequestDto.setAvailable(true);

        itemResponseDto.setId(1L);
        itemResponseDto.setName("itemTest");
        itemResponseDto.setDescription("Test description");
        itemResponseDto.setAvailable(true);
    }

    @Test
    void createItemTest() throws Exception {
        when(itemService.create(itemRequestDto, user.getId())).thenReturn(itemResponseDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(itemResponseDto.getDescription())));
    }
}

package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private final User user = new User();
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
    void createItemTest() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemMapper.mapToItem(itemRequestDto, user)).thenReturn(itemTest);
        when(itemMapper.mapToItemResponseWithoutRequestDto(itemTest)).thenReturn(itemResponseDto);
        when(itemRepository.save(itemTest)).thenReturn(itemTest);

        ItemResponseDto itemResult = itemService.create(itemRequestDto, 1L);

        assertEquals(itemResult, itemResponseDto);
    }
}

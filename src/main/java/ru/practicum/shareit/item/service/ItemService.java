package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto create(Item item, long userId);

    ItemDto update(long id, Item item, long userId);

    List<ItemDto> get(long userId);

    ItemDto getItemById(long id);

    List<ItemDto> searchItem(String text);
}

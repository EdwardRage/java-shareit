package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto create(Item item, long userId);

    ItemDto update(long id, Item item, long userId);

    List<ItemDto> get(long userId);

    ItemDto getItemById(long itemId, long userId);

    List<ItemDto> searchItem(String text);

    CommentResponse createComment(long itemId, long authorId, Comment comment);
}

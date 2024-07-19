package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item create(Item item, long userId);

    Item update(long id, Item item);

    List<Item> get(long userId);

    Optional<Item> getItemById(Long id);

    List<Item> searchItem(String text);
}

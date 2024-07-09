package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> itemMap = new HashMap<>();
    private int id = 0;

    @Override
    public Item create(Item item, long userId) {
        item.setId(generatedId());
        item.setOwner(userId);
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(long id, Item item) {
        itemMap.put(id, item);
        return item;
    }

    @Override
    public List<Item> get(long userId) {
        return itemMap.values().stream()
                .filter(item -> item.getOwner() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        try {
            Item item = itemMap.get(id);
            return Optional.ofNullable(item);
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> searchItem(String text) {
        return itemMap.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                                || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(item -> item.getAvailable())
                .collect(Collectors.toList());
    }

    private long generatedId() {
        id++;
        return id;
    }
}

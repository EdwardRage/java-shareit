package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(Item item, long userId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        validateCreate(item);
        Item itemFull = itemRepository.create(item, user.getId());
        return ItemMapper.mapToItemDto(itemFull);
    }

    @Override
    public ItemDto update(long id, Item item, long userId) {
        Item oldItem = itemRepository.getItemById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));
        if (oldItem.getOwner() == userId) {
            if (item.getName() != null) {
                oldItem.setName(item.getName());
            }
            if (item.getDescription() != null ) {
                oldItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                oldItem.setAvailable(item.getAvailable());
            }
            Item oldItemFull = itemRepository.update(id, oldItem);
            return ItemMapper.mapToItemDto(oldItemFull);
        }
        throw new NotFoundException("Пользователь userId = " + userId +
                " не является владельцем вещи itemId=" + id);

    }

    @Override
    public List<ItemDto> get(long userId) {
        return itemRepository.get(userId).stream()
                .map(ItemMapper::mapToItemDto)
                //.filter(itemDto -> itemDto.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(long id) {
        return itemRepository.getItemById(id)
                .map(ItemMapper::mapToItemDto)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItem(text).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    private void validateCreate(Item item) {
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new ConditionsNotMetException("Вещь не может быть без названия");
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new ConditionsNotMetException("Вещь не может быть без описания");
        }
        if (item.getAvailable() == null) {
            throw new ConditionsNotMetException("У вещи должен быть статус");
        }
    }
}

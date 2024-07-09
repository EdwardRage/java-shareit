package ru.practicum.shareit.item.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> get(@NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.get(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @PostMapping
    public ItemDto create(@RequestBody Item item, @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.create(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId, @RequestBody Item item,
                          @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.update(itemId, item, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@NotNull @RequestParam String text) {
        return itemService.searchItem(text);
    }
}

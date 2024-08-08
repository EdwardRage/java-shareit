package ru.practicum.shareit.item.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> get(@NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        List<ItemDto> listItem = itemService.get(userId);
        log.info("Items by userId ==> " + listItem);
        return listItem;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId,
                               @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @PostMapping
    public ItemDto create(@RequestBody Item item, @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        ItemDto itemDto = itemService.create(item, userId);
        log.info("Create item ==> " + itemDto);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId, @RequestBody Item item,
                          @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        ItemDto itemDto = itemService.update(itemId, item, userId);
        log.info("Update item ==> " + itemDto);
        return itemDto;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@NotNull @RequestParam String text) {
        return itemService.searchItem(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentResponse createComment(@NotNull @PathVariable long itemId,
                                         @NotNull @RequestHeader("X-Sharer-User-Id") long authorId,
                                         @RequestBody Comment comment) {
        CommentResponse commentResponse = itemService.createComment(itemId, authorId, comment);
        log.info("Create comment ==> " + commentResponse);
        return commentResponse;
    }
}

package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.get(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@NotNull @PathVariable long itemId,
                                              @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemRequestDto item,
                                         @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.create(item, userId);
    }

    //ItemDto исправить на нужное ДТО
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable long itemId, @RequestBody @Valid ItemForUpdateDto item,
                                         @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.update(itemId, item, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable long itemId,
                                         @RequestHeader("X-Sharer-User-Id") long authorId,
                                         @RequestBody CommentRequest comment) {
        return itemClient.createComment(itemId, authorId, comment);
    }
}

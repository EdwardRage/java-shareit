
package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;
    private final CommentMapper commentMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponseDto create(ItemRequestDto itemDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Request request = new Request();
        if (itemDto.getRequestId() != null) {
            request = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с id = " + itemDto.getRequestId() + " не найден"));
            Item item = itemMapper.mapToItem(itemDto, user, request);

            item = itemRepository.save(item);
            return itemMapper.mapToItemResponseDto(item);
        } else {
            Item item = itemMapper.mapToItem(itemDto, user);
            item = itemRepository.save(item);
            return itemMapper.mapToItemResponseWithoutRequestDto(item);
        }
    }

    @Override
    @Transactional
    public ItemDto update(long id, Item item, long userId) {
        Item oldItem = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));
        if (oldItem.getUser().getId() == userId) {
            if (item.getName() != null) {
                oldItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                oldItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                oldItem.setAvailable(item.getAvailable());
            }
            Item oldItemFull = itemRepository.save(oldItem);
            return itemMapper.mapToItemDto(oldItemFull);
        }
        throw new NotFoundException("Пользователь userId = " + userId +
                " не является владельцем вещи itemId=" + id);
    }

    @Override
    public List<ItemDto> get(long userId) {
        return itemRepository.findByUserId(userId).stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));

        if (item.getUser().getId() == userId) {
            Booking bookingLast = bookingRepository.findFirstByItem_IdAndEndRentBeforeOrderByEndRentDesc(item.getId(), LocalDateTime.now());
            Booking bookingNext = bookingRepository.findFirstByItem_IdAndStartAfterOrderByStart(item.getId(), LocalDateTime.now());
            List<Comment> comment = commentRepository.findAllByItem_Id(item.getId());
            List<CommentResponse> commentsResponse = comment.stream().map(commentMapper::mapToCommentResponse).toList();
            return itemMapper.mapToItemForOwner(item, bookingLast, bookingNext, commentsResponse);
        } else {
            List<Comment> comment = commentRepository.findAllByItem_Id(item.getId());
            List<CommentResponse> commentsResponse = comment.stream().map(commentMapper::mapToCommentResponse).toList();
            return itemMapper.mapToForBooker(item, commentsResponse);
        }
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.findItemsByText(text).stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse createComment(long itemId, long authorId, Comment comment) {
        Booking bookingWithItemId = bookingRepository.findByItem_Id(itemId, authorId)
                .orElseThrow(() -> new NotFoundException("Вещь с itemId = " + itemId + " не брали в аренду " +
                        "или ее не существует"));

        if (bookingWithItemId.getEndRent().isBefore(LocalDateTime.now())) {
            comment.setItem(bookingWithItemId.getItem());
            comment.setAuthor(bookingWithItemId.getBooker());
            comment.setCreated(LocalDateTime.now());
            Comment commentFull = commentRepository.save(comment);
            return commentMapper.mapToCommentResponse(commentFull);
        }
        throw new ConditionsNotMetException("Комментарий можно оставить после окончания аренды");
    }

    /*private void validateCreate(ItemRequestDto item) {
        if (item.getName() == null || item.getName().isEmpty() || item.getName().isBlank()) {
            throw new ConditionsNotMetException("Вещь не может быть без названия");
        }
        if (item.getDescription() == null || item.getDescription().isEmpty() || item.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Вещь не может быть без описания");
        }
        if (item.getAvailable() == null) {
            throw new ConditionsNotMetException("У вещи должен быть статус");
        }
    }*/
}

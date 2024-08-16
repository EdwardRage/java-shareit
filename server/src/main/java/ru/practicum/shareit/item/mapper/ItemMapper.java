package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public class ItemMapper {

    public ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public ItemDto mapToItemForOwner(Item item, Booking bookingLast, Booking bookingNext, List<CommentResponse> comments) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        itemDto.setLastBooking(bookingLast != null ?
                new ItemDto.BookingLastDto(bookingLast.getId(), bookingLast.getBooker().getId()) : null);
        itemDto.setNextBooking(bookingNext != null ?
                new ItemDto.BookingNextDto(bookingNext.getId(), bookingNext.getBooker().getId()) : null);

        itemDto.setComments(comments);

        return itemDto;
    }

    public ItemDto mapToForBooker(Item item, List<CommentResponse> comments) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(comments);
        return itemDto;
    }

    public ItemResponseDto mapToItemResponseDto(Item item) {
        ItemResponseDto itemDto = new ItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        itemDto.setRequestId(item.getRequest().getId());
        return itemDto;
    }
    public ItemResponseDto mapToItemResponseWithoutRequestDto(Item item) {
        ItemResponseDto itemDto = new ItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }
    public Item mapToItem(ItemRequestDto itemDto, User user, Request request) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setUser(user);
        item.setRequest(request);
        return item;
    }

    public Item mapToItem(ItemRequestDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setUser(user);
        return item;
    }
}

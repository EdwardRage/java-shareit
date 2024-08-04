package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

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
}

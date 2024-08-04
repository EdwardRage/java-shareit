package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDtoForBooking;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapper {
    public Booking mapToNewBooking(BookingRequest bookingRequest, Item item, User user) {
        Booking booking = new Booking();
        booking.setStart(bookingRequest.getStart());
        booking.setEndRent(bookingRequest.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingState.WAITING);
        return booking;
    }

    public BookingDtoResponse mapToBookingDto(Booking booking) {
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(booking.getId());
        bookingDtoResponse.setStart(booking.getStart());
        bookingDtoResponse.setEnd(booking.getEndRent());

        UserDtoForBooking userDto = mapToUserDtoForResponse(booking.getBooker());
        bookingDtoResponse.setBooker(userDto);

        ItemDtoForBooking itemDto = mapToItemDtoForBooking(booking.getItem());
        bookingDtoResponse.setItem(itemDto);

        bookingDtoResponse.setStatus(booking.getStatus());
        return bookingDtoResponse;
    }

    private ItemDtoForBooking mapToItemDtoForBooking(Item item) {
        ItemDtoForBooking itemDto = new ItemDtoForBooking();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        return itemDto;
    }

    private UserDtoForBooking mapToUserDtoForResponse(User user) {
        UserDtoForBooking userDto = new UserDtoForBooking();
        userDto.setId(user.getId());
        return userDto;
    }
}

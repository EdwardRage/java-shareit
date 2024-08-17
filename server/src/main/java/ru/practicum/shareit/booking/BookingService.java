package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.util.List;

public interface BookingService {

    BookingDtoResponse create(BookingRequest booking, long userId);

    BookingDtoResponse setApprove(long bookerId, long ownerId, boolean approve);

    BookingDtoResponse getBookingById(long userId, long bookingId);

    List<BookingDtoResponse> getBookingByBookerId(String state, long bookerId);

    List<BookingDtoResponse> getBookingByOwnerId(long userId, String state);
}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingRequest;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@RequestBody BookingRequest bookingRequest,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        BookingDtoResponse booking = bookingService.create(bookingRequest, userId);
        log.info("booking is create ==>" + booking);
        return booking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse bookingApprove(@PathVariable long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") long ownerId,
                                             @RequestParam boolean approved) {
        BookingDtoResponse booking = bookingService.setApprove(bookingId, ownerId, approved);
        log.info("Booking approved ==> " + approved);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBookingById(@PathVariable long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        BookingDtoResponse booking = bookingService.getBookingById(userId, bookingId);
        log.info("Get booking by ID ==> " + booking);
        return booking;
    }

    @GetMapping
    public List<BookingDtoResponse> getBookingByBookerId(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        List<BookingDtoResponse> bookings = bookingService.getBookingByBookerId(state, userId);
        log.info("get Booking By Booker Id ==> " + bookings);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getBookingByOwnerId(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                                        @RequestHeader("X-Sharer-User-Id") long ownerId) {
        List<BookingDtoResponse> bookings = bookingService.getBookingByOwnerId(ownerId, state);
        log.info("get Booking By Booker Id ==> " + bookings);
        return bookings;
    }


}

package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDtoForBooking;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User userOwner;
    private User userBooker;
    private Item item;
    private Booking booking;
    private BookingRequest bookingRequest;
    private BookingDtoResponse bookingResponse;

    @BeforeEach
    void setUpEntity() {
        userOwner = new User();
        userOwner.setId(1L);
        userOwner.setName("Test User");
        userOwner.setEmail("test@test.ru");

        userBooker = new User();
        userBooker.setId(2L);
        userBooker.setName("Test booker");
        userBooker.setEmail("test@booker.ru");

        item = new Item();
        item.setId(1L);
        item.setName("item test");
        item.setDescription("description test");
        item.setUser(userOwner);
        item.setAvailable(true);

        bookingRequest = new BookingRequest();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.of(LocalDate.of(2025, 8, 16), LocalTime.now()));

        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(userBooker);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(bookingRequest.getStart());
        booking.setEndRent(bookingRequest.getEnd());

        bookingResponse = new BookingDtoResponse();
        bookingResponse.setId(booking.getId());
        bookingResponse.setStart(booking.getStart());
        bookingResponse.setEnd(booking.getEndRent());
        bookingResponse.setStatus(booking.getStatus());
        bookingResponse.setItem(new ItemDtoForBooking(item.getId(), item.getName()));
        bookingResponse.setBooker(new UserDtoForBooking(booking.getId()));
    }

    @Test
    void createBookingTest() {
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(userBooker));
        Mockito.when(itemRepository.findById(bookingRequest.getItemId())).thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingMapper.mapToNewBooking(bookingRequest, item, userBooker)).thenReturn(booking);
        Mockito.when(bookingMapper.mapToBookingDto(booking)).thenReturn(bookingResponse);
        Mockito.when(bookingRepository.save(booking)).thenReturn(booking);

        BookingDtoResponse bookingResult = bookingService.create(bookingRequest, userBooker.getId());

        assertEquals(bookingResponse, bookingResult);
    }
}

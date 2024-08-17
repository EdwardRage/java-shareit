package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDtoForBooking;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

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
    void createBookingTest() throws Exception {
        Mockito.when(bookingService.create(bookingRequest, userBooker.getId())).thenReturn(bookingResponse);

        mvc.perform(post("/bookings")
                    .content(mapper.writeValueAsString(bookingRequest))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", userBooker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponse.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingResponse.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingResponse.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(bookingResponse.getStatus().toString())));
    }
}

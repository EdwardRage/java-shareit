package ru.practicum.shareit.booking;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(long userId, BookingRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> bookingApprove(long bookingId, long ownerId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved=" + approved, ownerId, parameters);
    }

    public ResponseEntity<Object> getBookingById(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingByBookerId(String state, long bookerId) {
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("", bookerId, parameters);
    }

    public ResponseEntity<Object> getBookingByOwnerId(String state, long ownerId) {
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("", ownerId, parameters);
    }

}

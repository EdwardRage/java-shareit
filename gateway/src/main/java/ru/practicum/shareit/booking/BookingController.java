package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid BookingRequestDto bookingDto,
									 @RequestHeader("X-Sharer-User-Id") long userId) {
		log.info("Creating booking {}, userId={}", bookingDto, userId);
		return bookingClient.create(userId, bookingDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> bookingApprove(@NotNull @PathVariable long bookingId,
											 @NotNull @RequestHeader("X-Sharer-User-Id") long ownerId,
											 @NotNull @RequestParam(name = "approved") Boolean approved) {
		log.info("booking {} is approved {} ", bookingId, approved);
		return bookingClient.bookingApprove(bookingId, ownerId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
												 @NotNull @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBookingById(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingByBookerId(@RequestParam(value = "state", defaultValue = "ALL") String state,
													   @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
		log.info("get booking by bookerId = {}", userId);
		return bookingClient.getBookingByBookerId(state, userId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingByOwnerId(@RequestParam(value = "state", defaultValue = "ALL") String state,
													  @NotNull @RequestHeader("X-Sharer-User-Id") long ownerId) {
		log.info("");
		return bookingClient.getBookingByOwnerId(state, ownerId);
	}

}

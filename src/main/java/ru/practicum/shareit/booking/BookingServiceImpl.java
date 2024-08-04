package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDtoResponse create(BookingRequest bookingRequest, long bookerId) {
        User user = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + bookerId + " не найден"));

        Item item = itemRepository.findById(bookingRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + bookingRequest.getItemId() + " не найдена"));
        if (item.getUser().getId() == user.getId()) {
            throw new ConditionsNotMetException("Нельзя арендовать вещь у самого себя");
        }
        if (item.getAvailable()) {
            Booking booking = bookingMapper.mapToNewBooking(bookingRequest, item, user);
            if (booking.getStart().isAfter(booking.getEndRent()) || booking.getStart().equals(booking.getEndRent())) {
                throw new ConditionsNotMetException("Начало аренды должно быть раньше, чем ее окончание");
            }
            booking = bookingRepository.save(booking);
            return bookingMapper.mapToBookingDto(booking);
        }
        throw new NotAvailableException("Вещь с id = " + item.getId() + " сейчас не доступна");
    }

    @Override
    public BookingDtoResponse setApprove(long bookingId, long ownerId, boolean approve) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id = " + bookingId + " не найдено"));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ConditionsNotMetException("Пользователь с id = " + ownerId + " не найден"));

        if (booking.getItem().getUser().getId() == owner.getId()) {
            if (booking.getStatus().equals(BookingState.WAITING)) {
                if (approve) {
                    booking.setStatus(BookingState.APPROVED);
                    bookingRepository.save(booking);
                } else {
                    booking.setStatus(BookingState.REJECTED);
                    bookingRepository.save(booking);
                }
                BookingDtoResponse bookingDto = bookingMapper.mapToBookingDto(booking);
                return bookingDto;
            } else {
                throw new ConditionsNotMetException("Нельзя изменить статус бронирования после одобрения/отказа");
            }
        }
        throw new ConditionsNotMetException("User с id = " + owner.getId() + " не является владельцем " +
                "вещи itemId " + booking.getItem().getId());
    }

    @Override
    public BookingDtoResponse getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id = " + bookingId + " не найдено"));
        long ownerId = booking.getItem().getUser().getId();
        long bookerId = booking.getBooker().getId();
        if (userId == ownerId || userId == bookerId) {
            BookingDtoResponse bookingDto = bookingMapper.mapToBookingDto(booking);
            return bookingDto;
        }
        throw new ConditionsNotMetException("Пользователь userId=" + userId +
                " не может получить данные о бронировании bookingId=" + bookingId);
    }

    @Override
    public List<BookingDtoResponse> getBookingByBookerId(String state, long userId) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        long bookerId = booker.getId();
        switch (state) {
            case "ALL":
                List<BookingDtoResponse> bookingAll = bookingRepository.findByBooker_IdOrderByStartDesc(bookerId).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingAll;
            case "CURRENT":
                List<BookingDtoResponse> bookingCurrent = bookingRepository.findByBooker_IdAndStatusAndEndRentAfterOrderByStartDesc(
                        bookerId, BookingState.APPROVED, LocalDateTime.now()).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingCurrent;
            case "PAST":
                List<BookingDtoResponse> bookingPast = bookingRepository.findByBooker_IdAndStatusAndEndRentBeforeOrderByStartDesc(
                                bookerId, BookingState.APPROVED, LocalDateTime.now()).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingPast;
            case "FUTURE":
                List<BookingDtoResponse> bookingFuture = bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(
                                bookerId, LocalDateTime.now()).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingFuture;
            case "WAITING":
                List<BookingDtoResponse> bookingWaiting = bookingRepository.findByBooker_IdAndStatusOrderByStart(
                        bookerId, BookingState.WAITING).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingWaiting;
            case "REJECTED":
                List<BookingDtoResponse> bookingRegected = bookingRepository.findByBooker_IdAndStatusOrderByStart(
                                bookerId, BookingState.REJECTED).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingRegected;
        }
        throw new ConditionsNotMetException("Unknown state: UNSUPPORTED_STATUS");
    }

    @Override
    public List<BookingDtoResponse> getBookingByOwnerId(long userId, String state) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        long ownerId = booker.getId();
        switch (state) {
            case "ALL":
                List<BookingDtoResponse> bookingAll = bookingRepository.findAllByOwnerId(
                                ownerId).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingAll;
            case "CURRENT":
                List<BookingDtoResponse> bookingCurrent = bookingRepository.findByOwnerIdCurrent(
                        ownerId, BookingState.APPROVED, LocalDateTime.now()).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingCurrent;
            case "PAST":
                List<BookingDtoResponse> bookingPast = bookingRepository.findByOwnerIdPast(
                                ownerId, BookingState.APPROVED, LocalDateTime.now()).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingPast;
            case "FUTURE":
                List<BookingDtoResponse> bookingFuture = bookingRepository.findByOwnerIdFuture(
                                ownerId, LocalDateTime.now()).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingFuture;
            case "WAITING":
                List<BookingDtoResponse> bookingWaiting = bookingRepository.findByOwnerIdByStatus(
                                ownerId, BookingState.WAITING).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingWaiting;
            case "REJECTED":
                List<BookingDtoResponse> bookingRegected = bookingRepository.findByOwnerIdByStatus(
                                ownerId, BookingState.REJECTED).stream()
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
                return bookingRegected;
        }
        throw new ConditionsNotMetException("Unknown state: UNSUPPORTED_STATUS");
    }
}

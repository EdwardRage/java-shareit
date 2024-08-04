package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBooker_IdOrderByStartDesc(long bookerId);

    List<Booking> findByBooker_IdAndStatusAndEndRentAfterOrderByStartDesc(long bookerId, BookingState bookingState, LocalDateTime time);

    List<Booking> findByBooker_IdAndStatusAndEndRentBeforeOrderByStartDesc(long bookerId, BookingState bookingState, LocalDateTime time);

    List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime time);

    List<Booking> findByBooker_IdAndStatusOrderByStart(long bookerId, BookingState bookingState);

    @Query("select bk from Booking as bk " +
            "join bk.item as it " +
            "join it.user as u " +
            "where u.id = :ownerId " +
            "order by bk.start desc")
    List<Booking> findAllByOwnerId(long ownerId);

    @Query("select bk from Booking as bk " +
            "join bk.item as it " +
            "join it.user as u " +
            "where u.id = :ownerId and bk.status = :status and bk.endRent >= :time " +
            "order by bk.start desc")
    List<Booking> findByOwnerIdCurrent(long ownerId, BookingState status, LocalDateTime time);

    @Query("select bk from Booking as bk " +
            "join bk.item as it " +
            "join it.user as u " +
            "where u.id = :ownerId and bk.status = :status and bk.endRent <= :time " +
            "order by bk.start desc")
    List<Booking> findByOwnerIdPast(long ownerId, BookingState status, LocalDateTime time);

    @Query("select bk from Booking as bk " +
            "join bk.item as it " +
            "join it.user as u " +
            "where u.id = :ownerId and bk.start >= :time " +
            "order by bk.start desc")
    List<Booking> findByOwnerIdFuture(long ownerId, LocalDateTime time);

    @Query("select bk from Booking as bk " +
            "join bk.item as it " +
            "join it.user as u " +
            "where u.id = :ownerId and bk.status = :status")
    List<Booking> findByOwnerIdByStatus(long ownerId, BookingState status);

    @Query("select bk from Booking as bk " +
            "join bk.item as it " +
            "join bk.booker as bkr " +
            "where it.id = :itemId and bk.status = 'APPROVED' and bkr.id = :bookerId")
    Optional<Booking> findByItem_Id(long itemId, long bookerId);

    Booking findFirstByItem_IdAndEndRentBeforeOrderByEndRentDesc(long itemId, LocalDateTime dateTime);

    Booking findFirstByItem_IdAndStartAfterOrderByStart(long itemId, LocalDateTime dateTime);
}

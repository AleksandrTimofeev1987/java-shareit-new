package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByBookerId(Long bookerId, Pageable page);

    Page<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime now, LocalDateTime now1, Pageable page);

    Page<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime now, Pageable page);

    Page<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime now, Pageable page);

    Page<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable page);

    Page<Booking> findByItemOwnerId(Long ownerId, Pageable page);

    Page<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime now, LocalDateTime now1, Pageable page);

    Page<Booking> findByItemOwnerIdAndEndIsBefore(Long ownerId, LocalDateTime now, Pageable page);

    Page<Booking> findByItemOwnerIdAndStartIsAfter(Long ownerId, LocalDateTime now, Pageable page);

    Page<Booking> findByItemOwnerIdAndStatus(Long ownerId, BookingStatus rejected, Pageable page);

    Booking findFirstByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime now);
    Booking findFirstByItemIdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    Boolean existsByBookerIdAndItemIdAndEndIsBefore(Long bookerId, Long itemId, LocalDateTime now);
}

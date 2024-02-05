package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validator.BookingValidator;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private static final Sort SORT_BY_START = Sort.by(Sort.Direction.DESC, "start");
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookingsByBooker(Long userId, RequestState state) {
        log.debug("Request to find bookings made by user with ID - {} is received.", userId);

        validateUserExists(userId);


        List<Booking> foundBookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        foundBookings = getBookingsByBooker(foundBookings, userId, state, now);

        log.debug("List of bookings made by user with ID - {} is received from the repository.", userId);
        return foundBookings
                .stream()
                .map(bookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookingsByOwner(Long userId, RequestState state) {
        log.debug("Request to find bookings made for items owned by user with ID - {} is received.", userId);

        validateUserExists(userId);

        List<Booking> foundBookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        foundBookings = getBookingsByOwner(foundBookings, userId, state, now);

        log.debug("List of bookings made by user with ID - {} is received from the repository.", userId);
        return foundBookings
                .stream()
                .map(bookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        log.debug("Request to find booking with ID - {} is received from user with ID - {}.", bookingId, userId);

        validateUserExists(userId);

        Booking foundBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format("Booking with id: %d is not found", bookingId)));
        validateUserIsBookerOrOwner(userId, foundBooking);

        log.debug("Booking with ID - {} is found in the repository.", foundBooking.getId());
        return bookingMapper.toBookingResponseDto(foundBooking);
    }

    @Override
    @Transactional
    public BookingResponseDto createBooking(Long userId, Long itemId, Booking booking) {
        log.debug("Request to add new booking for item with ID - {} is received from user with ID - {}.", itemId, userId);

        Item bookedItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", itemId)));
        BookingValidator.validateItemIsAvailable(bookedItem.getAvailable());
        BookingValidator.validateUserIsNotOwner(userId, bookedItem.getOwner().getId());

        booking.setItem(bookedItem);
        booking.setBooker(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId))));

        Booking savedBooking = bookingRepository.save(booking);

        log.debug("Booking with ID - {} is added to repository by user with ID - {}.", savedBooking.getId(), savedBooking.getBooker().getId());

        return bookingMapper.toBookingResponseDto(savedBooking);
    }

    @Override
    public BookingResponseDto updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        log.debug("Request to update status of booking with ID - {} is received from user with ID - {}. Approved set to - {}", bookingId, userId, approved);

        validateUserExists(userId);

        Booking bookingForUpdate = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format("Booking with id: %d is not found", bookingId)));
        validateItemBelongsToUser(userId, bookingForUpdate.getItem().getOwner().getId());

        if (approved) {
            BookingValidator.validateBookingIsNotApproved(bookingForUpdate.getStatus());
            bookingForUpdate.setStatus(BookingStatus.APPROVED);
        } else {
            bookingForUpdate.setStatus(BookingStatus.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(bookingForUpdate);

        log.debug("Status of booking with ID - {} is updated in repository by user with ID - {} to {}.", updatedBooking.getId(), userId, updatedBooking.getStatus().toString());
        return bookingMapper.toBookingResponseDto(updatedBooking);
    }

    private List<Booking> getBookingsByBooker(List<Booking> foundBookings, Long bookerId, RequestState state, LocalDateTime now) {
        switch (state) {
            case ALL:
                foundBookings = bookingRepository.findByBookerId(bookerId, SORT_BY_START);
                break;
            case CURRENT:
                foundBookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(bookerId, now, now, SORT_BY_START);
                break;
            case PAST:
                foundBookings = bookingRepository.findByBookerIdAndEndIsBefore(bookerId, now, SORT_BY_START);
                break;
            case FUTURE:
                foundBookings = bookingRepository.findByBookerIdAndStartIsAfter(bookerId, now, SORT_BY_START);
                break;
            case WAITING:
                foundBookings = bookingRepository.findByBookerIdAndStatus(bookerId, BookingStatus.WAITING, SORT_BY_START);
                break;
            case REJECTED:
                foundBookings = bookingRepository.findByBookerIdAndStatus(bookerId, BookingStatus.REJECTED, SORT_BY_START);
                break;
        }
        return foundBookings;
    }

    private List<Booking> getBookingsByOwner(List<Booking> foundBookings, Long ownerId, RequestState state, LocalDateTime now) {
        switch (state) {
            case ALL:
                foundBookings = bookingRepository.findByItemOwnerId(ownerId, SORT_BY_START);
                break;
            case CURRENT:
                foundBookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(ownerId, now, now, SORT_BY_START);
                break;
            case PAST:
                foundBookings = bookingRepository.findByItemOwnerIdAndEndIsBefore(ownerId, now, SORT_BY_START);
                break;
            case FUTURE:
                foundBookings = bookingRepository.findByItemOwnerIdAndStartIsAfter(ownerId, now, SORT_BY_START);
                break;
            case WAITING:
                foundBookings = bookingRepository.findByItemOwnerIdAndStatus(ownerId, BookingStatus.WAITING, SORT_BY_START);
                break;
            case REJECTED:
                foundBookings = bookingRepository.findByItemOwnerIdAndStatus(ownerId, BookingStatus.REJECTED, SORT_BY_START);
                break;
        }
        return foundBookings;
    }

    private void validateUserIsBookerOrOwner(Long userId, Booking foundBooking) {
        if (!(userId.equals(foundBooking.getBooker().getId()) || userId.equals(foundBooking.getItem().getOwner().getId()))) {
            final String message = String.format("User with id: %d is not booker or item owner for booking with id: %d", userId, foundBooking.getId());
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            final String message = String.format("User with id: %d is not found", userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    private void validateItemBelongsToUser(Long userId, Long ownerId) {
        if (!ownerId.equals(userId)) {
            final String message = String.format("User with id: %d does not own item. Booking status cannot be updated.", userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }
}

package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.RequestState;

import java.util.List;

public interface BookingService {

    /**
     * Method return bookings made by user.
     *
     * @param userId ID of user requesting bookings.
     * @param state Request state for selection.
     *
     * @return List of booking response DTO's.
     */
    List<BookingResponseDto> getAllBookingsByBooker(Long userId, RequestState state);

    /**
     * Method return bookings made for user's items.
     *
     * @param userId ID of user requesting bookings.
     * @param state Request state for selection.
     *
     * @return List of bookings response DTO's.
     */
    List<BookingResponseDto> getAllBookingsByOwner(Long userId, RequestState state);

    /**
     * Method return requested booking.
     *
     * @param userId ID of user requesting booking.
     * @param bookingId ID of booking to be found.
     *
     * @return Found booking response DTO .
     */
    BookingResponseDto getBooking(Long userId, Long bookingId);

    /**
     * Method creates new booking.
     *
     * @param userId ID of user adding booking.
     * @param itemId ID of item to be booked.
     * @param booking Booking to be added.
     *
     * @return Added booking response DTO with assigned ID.
     */
    BookingResponseDto createBooking(Long userId, Long itemId, Booking booking);

    /**
     * Method updates status of the booking.
     *
     * @param userId ID of user updating booking status.
     * @param bookingId ID of booking.
     * @param approved Status to be set: true = APPROVED, false = REJECTED.
     *
     * @return Updated booking response DTO.
     */
    BookingResponseDto updateBookingStatus(Long userId, Long bookingId, Boolean approved);
}

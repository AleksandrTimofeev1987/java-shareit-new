package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.validator.BookingValidator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping
    private List<BookingResponseDto> getAllBookingsByBooker(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                            @RequestParam(required = false, defaultValue = "ALL") RequestState state) {
        return bookingService.getAllBookingsByBooker(userId, state);
    }

    @GetMapping("/owner")
    private List<BookingResponseDto> getAllBookingsByOwner(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                           @RequestParam(required = false, defaultValue = "ALL") RequestState state) {
        return bookingService.getAllBookingsByOwner(userId, state);
    }

    @GetMapping("/{bookingId}")
    private BookingResponseDto getBooking(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                          @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @PostMapping
    private BookingResponseDto createBooking(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                             @Valid @RequestBody BookingCreateDto bookingDto) {
        BookingValidator.validateBookingStartBeforeEnd(bookingDto);

        Booking booking = bookingMapper.toBooking(bookingDto);
        Long itemId = bookingDto.getItemId();

        return bookingService.createBooking(userId, itemId, booking);
    }

    @PatchMapping("/{bookingId}")
    private BookingResponseDto updateBookingStatus(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                   @PathVariable Long bookingId,
                                                   @RequestParam Boolean approved) {
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }

}

package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingResponseDto toBookingResponseDto(Booking booking);

    default BookingShortDto toBookingShortDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingShortDto bookingDto = new BookingShortDto();

        bookingDto.setId(booking.getId());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());

        return bookingDto;
    }

    default Booking toBooking(BookingCreateDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }

        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        return booking;
    }

}

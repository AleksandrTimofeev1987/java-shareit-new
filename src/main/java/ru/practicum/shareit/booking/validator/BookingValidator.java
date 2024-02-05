package ru.practicum.shareit.booking.validator;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.NotFoundException;

@Slf4j
public class BookingValidator {

    public static void validateBookingStartBeforeEnd(BookingCreateDto bookingDto) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            final String message = "Booking start date should be before booking end date";
            log.warn(message);
            throw new BadRequestException(message);
        }
    }

    public static void validateItemIsAvailable(Boolean available) {
        if (!available) {
            final String message = "Item to be booked should be available";
            log.warn(message);
            throw new BadRequestException(message);
        }

    }

    public static void validateUserIsNotOwner(Long userId, Long ownerId) {
        if (userId.equals(ownerId)) {
            final String message = "User cannot book own item";
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    public static void validateBookingIsNotApproved(BookingStatus status) {
        if (status.equals(BookingStatus.APPROVED)) {
            final String message = "Booking is already approved.";
            log.warn(message);
            throw new BadRequestException(message);
        }

    }
}

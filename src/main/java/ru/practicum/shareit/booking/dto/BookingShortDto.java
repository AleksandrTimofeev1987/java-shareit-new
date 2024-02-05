package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingShortDto {

    private Long id;
    private Long bookerId;
    private Long itemId;
    private BookingStatus status;
    private LocalDateTime start;
    private LocalDateTime end;

}

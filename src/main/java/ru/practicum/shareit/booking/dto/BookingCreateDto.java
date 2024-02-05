package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Valid
public class BookingCreateDto {

    @NotNull(message = "{item.booking.not_null}")
    private Long itemId;

    @NotNull(message = "{start.booking.not_null}")
    @Future(message = "{start.booking.future}")
    private LocalDateTime start;

    @NotNull(message = "{end.booking.not_null}")
    @Future(message = "{end.booking.future}")
    private LocalDateTime end;
}

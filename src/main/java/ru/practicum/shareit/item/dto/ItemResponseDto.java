package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserResponseDto owner;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
}

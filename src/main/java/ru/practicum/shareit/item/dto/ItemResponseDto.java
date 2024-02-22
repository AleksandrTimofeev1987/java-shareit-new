package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.HashSet;
import java.util.Set;

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
    private Set<CommentResponseDto> comments = new HashSet<>();

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Long requestId;
}

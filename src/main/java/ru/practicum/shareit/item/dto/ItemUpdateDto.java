package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Valid
public class ItemUpdateDto {

    private String name;

    @Size(max = 200, message = "{description.item.size}")
    private String description;

    private Boolean available;
}

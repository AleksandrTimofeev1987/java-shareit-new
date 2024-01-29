package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Valid
public class ItemCreateDto {
    @NotBlank(message = "{name.item.not_blank}")
    private String name;

    @NotBlank(message = "{description.item.not_blank}")
    @Size(max = 200, message = "{description.item.size}")
    private String description;

    @NotNull(message = "{available.item.not_null}")
    private Boolean available;

    private ItemRequest request;
}

package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Valid
@NoArgsConstructor
public class ItemRequestCreateDto {

    @NotBlank(message = "{description.request.not_blank}")
    @Size(max = 200, message = "{description.request.size}")
    private String description;

}

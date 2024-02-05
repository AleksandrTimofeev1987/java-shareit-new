package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Valid
public class UserUpdateDto {

    @Pattern(regexp = "^\\S*$", message = "{name.user.no_spaces}")
    @Size(max = 255, message = "{name.user.size}")
    private String name;

    @Email(message = "{email.user.not_valid}")
    @Size(max = 255, message = "{email.user.size}")
    private String email;
}

package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@Valid
public class UserUpdateDto {

    @Pattern(regexp = "^\\S*$", message = "{name.user.no_spaces}")
    private String name;

    @Email(message = "{email.user.not_valid}")
    private String email;
}

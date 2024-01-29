package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@Valid
public class UserCreateDto {

    @NotBlank(message = "{name.user.not_blank}")
    @Pattern(regexp = "^\\S*$", message = "{name.user.no_spaces}")
    private String name;

    @NotBlank(message = "{email.user.not_blank}")
    @Email(message = "{email.user.not_valid}")
    private String email;
}

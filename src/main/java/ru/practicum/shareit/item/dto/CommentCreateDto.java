package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class CommentCreateDto {

    @NotBlank(message = "{text.comment.not_blank}")
    @Size(max = 200, message = "{text.comment.size}")
    private String text;

}

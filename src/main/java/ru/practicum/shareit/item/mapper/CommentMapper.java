package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    default CommentResponseDto toCommentResponseDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setId(comment.getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setAuthorName(comment.getAuthor().getName());
        commentResponseDto.setCreated(comment.getCreated());

        return commentResponseDto;
    }

    default Comment toComment(CommentCreateDto commentDto) {
        if (commentDto == null) {
            return null;
        }

        Comment comment = new Comment();

        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now());

        return comment;
    }

}

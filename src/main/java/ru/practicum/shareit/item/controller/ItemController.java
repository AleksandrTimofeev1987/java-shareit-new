package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.PaginationValidator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping()
    private List<ItemResponseDto> getAllItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    private ItemResponseDto getItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                    @PathVariable Long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @PostMapping
    private ItemResponseDto createItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                       @Valid @RequestBody ItemCreateDto itemDto) {
        Item item = itemMapper.toItem(itemDto);

        return itemService.createItem(userId, item, itemDto.getRequestId());
    }

    @PatchMapping("/{itemId}")
    private ItemResponseDto updateItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                       @PathVariable Long itemId,
                                       @Valid @RequestBody ItemUpdateDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    private List<ItemResponseDto> search(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                         @RequestParam String text,
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        PaginationValidator.validatePaginationParameters(from, size);

        return itemService.search(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    private CommentResponseDto createComment(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentCreateDto commentDto) {
        Comment comment = commentMapper.toComment(commentDto);

        return itemService.createComment(userId, itemId, comment);
    }
}

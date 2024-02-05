package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping()
    private List<ItemResponseDto> getAllItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId) {
        return itemService.getAllItemsByUserId(userId);
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

        return itemService.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    private ItemResponseDto updateItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                       @PathVariable Long itemId,
                                       @Valid @RequestBody ItemUpdateDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    private List<ItemResponseDto> search(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                         @RequestParam String text) {
        return itemService.search(userId, text);
    }
}

package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validation.PaginationValidator;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @GetMapping("/all")
    private List<ItemRequestResponseDto> getAllItemRequests(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                                            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PaginationValidator.validatePaginationParameters(from, size);

        return itemRequestService.getAllItemRequests(userId, from, size);
    }

    @GetMapping
    private List<ItemRequestResponseDto> getOwnItemRequests(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId) {
        return itemRequestService.getOwnItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    private ItemRequestResponseDto getItem(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                           @PathVariable Long requestId) {
        return itemRequestService.getItem(userId, requestId);
    }

    @PostMapping
    private ItemRequestResponseDto createItemRequest(@RequestHeader(REQUEST_HEADER_USER_ID_TITLE) Long userId,
                                                     @Valid @RequestBody ItemRequestCreateDto itemRequestDto) {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);

        return itemRequestService.createItemRequest(userId, itemRequest);
    }
}

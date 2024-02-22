package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    /**
     * Method return all item requests.
     *
     * @param userId ID of user requesting item requests.
     * @param from Index of first element in response.
     * @param size Number elements to be shown.
     *
     * @return List of item request response DTOs.
     */
    List<ItemRequestResponseDto> getAllItemRequests(Long userId, Integer from, Integer size);

    /**
     * Method return all item requests created by user.
     *
     * @param userId ID of user requesting item requests.
     *
     * @return List of item request response DTOs.
     */
    List<ItemRequestResponseDto> getOwnItemRequests(Long userId);

    /**
     * Method gets item request by ID.
     *
     * @param userId ID of user requesting item.
     * @param requestId ID of item request to be returned.
     *
     * @return Item request with specific ID.
     */
    ItemRequestResponseDto getItem(Long userId, Long requestId);

    /**
     * Method creates new item request.
     *
     * @param userId ID of user adding item request.
     * @param itemRequest Item request to be added.
     *
     * @return Added item request response DTO with assigned ID.
     */
    ItemRequestResponseDto createItemRequest(Long userId, ItemRequest itemRequest);

}

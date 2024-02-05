package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    /**
     * Method gets all items owned by user.
     *
     * @param userId ID of user requesting the information.
     *
     * @return List of items owned by user.
     */
    List<ItemResponseDto> getAllItemsByUserId(Long userId);

    /**
     * Method gets item by ID.
     *
     * @param itemId ID of item to be returned.
     *
     * @return Item with specific ID.
     */
    ItemResponseDto getItem(Long itemId);

    /**
     * Method creates new item.
     *
     * @param userId ID of user adding item.
     * @param item Item to be added.
     *
     * @return Added item response DTO with assigned ID.
     */
    ItemResponseDto createItem(Long userId, Item item);

    /**
     * Method updates item.
     *
     * @param userId  User updating the item.
     * @param itemId  ID of item be updated.
     * @param itemDto Updated item.
     *
     * @return Updated item response DTO.
     */
    ItemResponseDto updateItem(Long userId, Long itemId, ItemUpdateDto itemDto);

    /**
     * Method gets all items which name or description contain text.
     *
     * @param userId ID of user requesting the information.
     * @param text Search request.
     *
     * @return List of items which name or description contain text.
     */
    List<ItemResponseDto> search(Long userId, String text);
}

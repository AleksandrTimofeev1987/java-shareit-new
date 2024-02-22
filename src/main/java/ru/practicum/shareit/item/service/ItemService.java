package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    /**
     * Method gets all items owned by user.
     *
     * @param userId ID of user requesting the information.
     * @param from Index of first element in response.
     * @param size Number elements to be shown.
     *
     * @return List of items owned by user.
     */
    List<ItemResponseDto> getAllItemsByUserId(Long userId, Integer from, Integer size);

    /**
     * Method gets item by ID.
     *
     * @param userId ID of user requesting item.
     * @param itemId ID of item to be returned.
     *
     * @return Item with specific ID.
     */
    ItemResponseDto getItem(Long userId, Long itemId);

    /**
     * Method creates new item.
     *
     * @param userId ID of user adding item.
     * @param item Item to be added.
     * @param requestId ID of request the item is created in response to.
     *
     * @return Added item response DTO with assigned ID.
     */
    ItemResponseDto createItem(Long userId, Item item, Long requestId);

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
     * @param from Index of first element in response.
     * @param size Number elements to be shown.
     *
     * @return List of items which name or description contain text.
     */
    List<ItemResponseDto> search(Long userId, String text, Integer from, Integer size);

    /**
     * Method creates new comment to item.
     *
     * @param userId ID of user adding comment.
     * @param itemId ID of item to be commented.
     * @param comment Comment to be added.
     *
     * @return Added comment response DTO with assigned ID.
     */
    CommentResponseDto createComment(Long userId, Long itemId, Comment comment);
}

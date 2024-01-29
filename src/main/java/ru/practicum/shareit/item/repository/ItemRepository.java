package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    /**
     * Method searches for all items owned by user in repository.
     *
     * @param userId ID of user.
     *
     * @return List of items owned by user.
     */
    List<Item> getAllItemsByUserId(Long userId);

    /**
     * Method searches for certain item in repository.
     *
     * @param itemId ID of item to be found.
     *
     * @return Found item or null.
     */
    Item findById(Long itemId);

    /**
     * Method saves new item in repository.
     *
     * @param item Item to be saved.
     *
     * @return Saved item with assigned ID.
     */
    Item save(Item item);

    /**
     * Method gets all items which name or description contain text from repository.
     *
     * @param text Search request.
     *
     * @return List of items which name or description contain text.
     */
    List<Item> search(String text);

    /**
     * Method checks if repository contain item with certain ID.
     *
     * @param itemId Item ID to be checked.
     */
    void checkItemExists(Long itemId);

    /**
     * Method checks if item belongs to certain user.
     *
     * @param userId User ID.
     * @param itemId Item ID to be checked.
     */
    void checkItemBelongsToUser(Long userId, Long itemId);

}

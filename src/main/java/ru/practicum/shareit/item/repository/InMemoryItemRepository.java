package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class InMemoryItemRepository implements ItemRepository {

    private Long globalId = 1L;
    private final Map<Long, Item> items = new HashMap<>();


    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        return items
                .values()
                .stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public Item save(Item item) {

        item.setId(globalId);
        items.put(globalId, item);

        globalId++;

        return items.get(item.getId());
    }

    @Override
    public List<Item> search(String text) {
        return items
                .values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    @Override
    public void checkItemExists(Long itemId) {
        if (findById(itemId) == null) {
            final String message = String.format("Item with ID - %d is not found.", itemId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public void checkItemBelongsToUser(Long userId, Long itemId) {
        if (!items.get(itemId).getOwner().equals(userId)) {
            final String message = String.format("Item with ID - %d does not belong to user with ID - %d.", itemId, userId);
            log.warn(message);
            throw new ForbiddenException(message);
        }
    }
}

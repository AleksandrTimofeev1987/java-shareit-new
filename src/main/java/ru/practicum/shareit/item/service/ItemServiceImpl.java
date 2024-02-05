package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public List<ItemResponseDto> getAllItemsByUserId(Long userId) {
        log.debug("Request to get all items owned by user with ID - {} is received.", userId);

        validateUserExists(userId);

        List<Item> items = itemRepository.findByOwnerIdOrderById(userId);

        log.debug("List of all items owned by user with ID - {} is received from repository.", userId);

        return items
                .stream()
                .map(itemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItem(Long itemId) {
        log.debug("Request to get item with ID - {} is received.", itemId);

        Item itemFromRepository = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", itemId)));

        log.debug("Item with ID - {} is received from repository.", itemFromRepository.getId());
        return itemMapper.toItemResponseDto(itemFromRepository);
    }

    @Override
    @Transactional
    public ItemResponseDto createItem(Long userId, Item item) {
        log.debug("Request to add new item with name - {} is received from user with ID - {}.", item.getName(), userId);

        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId))));

        Item savedItem = itemRepository.save(item);

        log.debug("Item with ID - {} is added to repository by user with ID - {}.", savedItem.getId(), savedItem.getOwner().getId());

        return itemMapper.toItemResponseDto(savedItem);
    }

    @Override
    @Transactional
    public ItemResponseDto updateItem(Long userId, Long itemId, ItemUpdateDto itemDto) {
        log.debug("Request to update item with ID - {} is received from user with ID - {}.", itemId, userId);

        validateUserExists(userId);

        Item itemForUpdate = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", itemId)));
        validateItemBelongsToUser(userId, itemForUpdate);

        itemMapper.toItemFromItemUpdateDto(itemDto, itemForUpdate);


        Item updatedItem = itemRepository.save(itemForUpdate);

        log.debug("Item with ID - {} is updated in repository by user with ID - {}.", updatedItem.getId(), userId);

        return itemMapper.toItemResponseDto(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> search(Long userId, String text) {
        log.debug("Request to get all items which name or description contain text - '{}' is received from user with ID - {}.", text, userId);

        validateUserExists(userId);

        if (text == null || text.equals("")) {
            return new ArrayList<>();
        }

        List<Item> searchResult = itemRepository.search(text);

        log.debug("List of all items which name or description contain text - '{}' is received from repository.", userId);
        return searchResult
                .stream()
                .map(itemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            final String message = String.format("User with id: %d is not found", userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    private void validateItemBelongsToUser(Long userId, Item item) {
        if (!item.getOwner().getId().equals(userId)) {
            final String message = String.format("User with id: %d does not own item with id: %d", userId, item.getId());
            log.warn(message);
            throw new ForbiddenException(message);
        }
    }
}

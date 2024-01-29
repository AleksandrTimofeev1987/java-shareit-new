package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    @Override
    public List<ItemResponseDto> getAllItemsByUserId(Long userId) {
        log.debug("Request to get all items owned by user with ID - {} is received.", userId);

        userRepository.checkUserExists(userId);

        List<Item> items = itemRepository.getAllItemsByUserId(userId);

        log.debug("List of all items owned by user with ID - {} is received from repository.", userId);

        return items
                .stream()
                .map(ItemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItem(Long itemId) {
        log.debug("Request to get item with ID - {} is received.", itemId);
        itemRepository.checkItemExists(itemId);

        Item itemFromRepository = itemRepository.findById(itemId);

        log.debug("Item with ID - {} is received from repository.", itemFromRepository.getId());
        return ItemMapper.toItemResponseDto(itemFromRepository);
    }

    @Override
    public ItemResponseDto createItem(Item item) {
        log.debug("Request to add new item with name - {} is received from user with ID - {}.", item.getName(), item.getOwner());

        userRepository.checkUserExists(item.getOwner());

        Item savedItem = itemRepository.save(item);

        log.debug("Item with ID - {} is added to repository by user with ID - {}.", savedItem.getId(), savedItem.getOwner());

        return ItemMapper.toItemResponseDto(savedItem);
    }

    @Override
    public ItemResponseDto updateItem(Long userId, Long itemId, ItemUpdateDto itemDto) {
        log.debug("Request to update item with ID - {} is received from user with ID - {}.", itemId, userId);

        userRepository.checkUserExists(userId);
        itemRepository.checkItemExists(itemId);
        itemRepository.checkItemBelongsToUser(userId, itemId);

        Item itemForUpdate = itemRepository.findById(itemId);

        ItemMapper.toItemFromItemUpdateDto(itemDto, itemForUpdate);

        Item updatedItem = itemRepository.findById(itemId);

        log.debug("Item with ID - {} is updated in repository by user with ID - {}.", updatedItem.getId(), userId);

        return ItemMapper.toItemResponseDto(updatedItem);
    }

    @Override
    public List<ItemResponseDto> search(Long userId, String text) {
        log.debug("Request to get all items which name or description contain text - '{}' is received from user with ID - {}.", text, userId);

        userRepository.checkUserExists(userId);

        if (text == null || text.equals("")) {
            return new ArrayList<>();
        }

        List<Item> searchResult = itemRepository.search(text.toLowerCase());

        log.debug("List of all items which name or description contain text - '{}' is received from repository.", userId);
        return searchResult
                .stream()
                .map(ItemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }
}

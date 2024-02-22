package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseForItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;
    private static final Sort SORT_BY_CREATED = Sort.by(Sort.Direction.DESC, "created");

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        log.debug("Request to get item requests is received from user with ID - {} .", userId);

        validateUserExists(userId);

        Pageable page = PageRequest.of(from / size, size, SORT_BY_CREATED);

        List<ItemRequest> foundItemRequests = itemRequestRepository.findItemRequestsByRequester_IdIsNot(userId, page).getContent();

        log.debug("List of all item requests is received from repository.");
        return foundItemRequests
                .stream()
                .map(itemRequestMapper::toItemRequestResponseDto)
                .map(this::populateWithResponses)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> getOwnItemRequests(Long userId) {
        log.debug("Request to get item requests created by user with ID - {} was received.", userId);

        validateUserExists(userId);

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);

        log.debug("List of all item requests created by user with ID - {} is received from repository.", userId);
        return itemRequests
                .stream()
                .map(itemRequestMapper::toItemRequestResponseDto)
                .map(this::populateWithResponses)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestResponseDto getItem(Long userId, Long requestId) {
        log.debug("Request to get item request with ID - {} is received.", requestId);

        validateUserExists(userId);

        ItemRequest itemRequestFromRepository = itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(String.format("Item request with id: %d is not found", requestId)));

        log.debug("Item request with ID - {} is received from repository.", itemRequestFromRepository.getId());

        ItemRequestResponseDto itemRequestDto = itemRequestMapper.toItemRequestResponseDto(itemRequestFromRepository);

        return populateWithResponses(itemRequestDto);
    }

    @Override
    @Transactional
    public ItemRequestResponseDto createItemRequest(Long userId, ItemRequest itemRequest) {

        log.debug("Request to add new item request is received from user with ID - {}.", userId);

        itemRequest.setRequester(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId))));

        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);

        log.debug("Item request with ID - {} is added to repository by user with ID - {}.", savedItemRequest.getId(), savedItemRequest.getRequester().getId());
        return itemRequestMapper.toItemRequestResponseDto(savedItemRequest);
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            final String message = String.format("User with id: %d is not found", userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    private ItemRequestResponseDto populateWithResponses(ItemRequestResponseDto itemRequestDto) {
        Set<Item> items = itemRepository.findAllByRequestId(itemRequestDto.getId());

        Set<ItemResponseForItemRequestDto> itemDtos = items
                .stream()
                .map(itemMapper::toItemResponseForItemRequestDto)
                .collect(Collectors.toSet());

        itemRequestDto.setItems(itemDtos);

        return itemRequestDto;
    }
}

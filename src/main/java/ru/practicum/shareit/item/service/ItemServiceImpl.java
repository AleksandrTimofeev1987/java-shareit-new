package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.BadRequestException;
import ru.practicum.shareit.exception.model.ForbiddenException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;
    private static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItemsByUserId(Long userId, Integer from, Integer size) {
        log.debug("Request to get all items owned by user with ID - {} is received.", userId);

        validateUserExists(userId);

        Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);

        List<Item> items = itemRepository.findByOwnerId(userId, page).getContent();

        log.debug("List of all items owned by user with ID - {} is received from repository.", userId);

        return items
                .stream()
                .map(itemMapper::toItemResponseDto)
                .map(item -> populateItemDtoWithBookingsAndComments(userId, item))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto getItem(Long userId, Long itemId) {
        log.debug("Request to get item with ID - {} is received.", itemId);

        validateUserExists(userId);

        Item itemFromRepository = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", itemId)));

        log.debug("Item with ID - {} is received from repository.", itemFromRepository.getId());

        ItemResponseDto itemDto = itemMapper.toItemResponseDto(itemFromRepository);

        return populateItemDtoWithBookingsAndComments(userId, itemDto);
    }

    @Override
    @Transactional
    public ItemResponseDto createItem(Long userId, Item item, Long requestId) {
        log.debug("Request to add new item with name - {} is received from user with ID - {}.", item.getName(), userId);

        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId))));

        if (requestId != null) {
            item.setRequest(itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(String.format("Item request with id: %d is not found", requestId))));
        }

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
    public List<ItemResponseDto> search(Long userId, String text, Integer from, Integer size) {
        log.debug("Request to get all items which name or description contain text - '{}' is received from user with ID - {}.", text, userId);

        validateUserExists(userId);

        if (text == null || text.equals("")) {
            return new ArrayList<>();
        }

        Pageable page = PageRequest.of(from / size, size, SORT_BY_ID);

        List<Item> searchResult = itemRepository.search(text, page).getContent();

        log.debug("List of all items which name or description contain text - '{}' is received from repository.", userId);
        return searchResult
                .stream()
                .map(itemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto createComment(Long userId, Long itemId, Comment comment) {
        log.debug("Request to add new comment to item with ID - {} is received from user with ID - {}.", itemId, userId);

        validateUserBookedItemAndBookingEnded(userId, itemId);

        comment.setAuthor(userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId))));
        comment.setItem(itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Item with id: %d is not found", itemId))));

        Comment savedComment = commentRepository.save(comment);

        log.debug("Comment with ID - {} is added to repository by user with ID - {}.", savedComment.getId(), savedComment.getAuthor().getId());
        return commentMapper.toCommentResponseDto(savedComment);
    }

    private ItemResponseDto populateItemDtoWithBookingsAndComments(Long userId, ItemResponseDto itemDto) {
        populateItemDtoWithBookings(userId, itemDto);
        populateItemDtoWithComments(itemDto);

        return itemDto;
    }

    private void populateItemDtoWithBookings(Long userId, ItemResponseDto itemDto) {
        if (userId.equals(itemDto.getOwner().getId())) {
            LocalDateTime now = LocalDateTime.now();

            final Long itemId = itemDto.getId();
            Booking lastBooking = bookingRepository.findFirstByItemIdAndEndIsBeforeOrderByEndDesc(itemId, now);
            Booking nextBooking = bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStartAsc(itemId, now);

            itemDto.setLastBooking(bookingMapper.toBookingShortDto(lastBooking));
            itemDto.setNextBooking(bookingMapper.toBookingShortDto(nextBooking));
        }
    }

    private void populateItemDtoWithComments(ItemResponseDto itemDto) {
        Set<Comment> comments = commentRepository.findAllByItemId(itemDto.getId());

        Set<CommentResponseDto> commentDtos = comments
                .stream()
                .map(commentMapper::toCommentResponseDto)
                .collect(Collectors.toSet());

        itemDto.setComments(commentDtos);
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

    private void validateUserBookedItemAndBookingEnded(Long bookerId, Long itemId) {
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndIsBefore(bookerId, itemId, LocalDateTime.now())) {
            final String message = String.format("User with id: %d has not booked item with id: %d or the booking has not expired", bookerId, itemId);
            log.warn(message);
            throw new BadRequestException(message);
        }
    }
}

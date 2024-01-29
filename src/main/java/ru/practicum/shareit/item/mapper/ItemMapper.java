package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemResponseDto toItemResponseDto(Item item) {
        if (item == null) {
            return null;
        }

        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }

    public static Item toItem(Long userId,
                              ItemCreateDto itemDto) {
        if (itemDto == null) {
            return null;
        }

        return new Item(
                null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userId,
                itemDto.getRequest() != null ? itemDto.getRequest() : null
        );
    }

    public static void toItemFromItemUpdateDto(ItemUpdateDto itemDto, Item item) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
    }
}

package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto getItemById(long itemId);

    ItemDto updateItem(long itemId, long userId, ItemDto itemDto);

    List<ItemDto> getUserItems(long userId);

    List<ItemDto> searchItems(String text);
}

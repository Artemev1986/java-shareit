package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseSimpleDto;

import java.util.List;

public interface ItemService {
    ItemResponseSimpleDto addItem(long userId, ItemRequestDto itemDto);

    ItemResponseSimpleDto getItemById(long itemId);

    ItemResponseDto getItemByIdAndUser(long userId, long itemId);

    ItemResponseSimpleDto updateItem(long itemId, long userId, ItemRequestDto itemDto);

    List<ItemResponseDto> getUserItems(long userId);

    List<ItemResponseSimpleDto> searchItems(String text);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}

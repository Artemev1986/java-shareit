package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemRequestDto;
import ru.practicum.shareit.dto.ItemResponseDto;
import ru.practicum.shareit.dto.ItemResponseSimpleDto;

import java.util.List;

public interface ItemService {
    ItemResponseSimpleDto addItem(long userId, ItemRequestDto itemDto);

    ItemResponseDto getItemByIdAndUser(long userId, long itemId);

    ItemResponseSimpleDto updateItem(long itemId, long userId, ItemRequestDto itemDto);

    List<ItemResponseDto> getUserItems(long userId);

    List<ItemResponseSimpleDto> searchItems(String text);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}

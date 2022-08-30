package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    public ItemServiceImpl(UserStorage userStorage, ItemStorage itemStorage) {
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id (" + userId + ") not found"));
        Item item = ItemMapper.toItem(itemDto);
        itemStorage.addItem(item);
        item.setOwnerId(userId);
        log.debug("Adding new item with id: {}", item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        ItemDto itemDto = ItemMapper.toItemDto(itemStorage.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id (" + itemId + ") not found")));
        log.debug("Item get by id: {}", itemId);
        return itemDto;
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, ItemDto itemDto) {
        Item itemFromMemory = itemStorage.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id (" + itemId + ") not found"));
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id (" + userId + ") not found"));
        Item item = ItemMapper.toItem(itemDto);
        if (itemFromMemory.getOwnerId() == userId) {
            item.setId(itemId);
            if (item.getName() == null) {
                item.setName(itemFromMemory.getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(itemFromMemory.getDescription());
            }

            item.setOwnerId(itemFromMemory.getOwnerId());

            if (item.getAvailable() == null) {
                item.setAvailable(itemFromMemory.getAvailable());
            }
            itemStorage.updateItem(item);
            log.debug("Item with id ({}) was updated", item.getId());
            return ItemMapper.toItemDto(item);
        } else {
            throw new NotFoundException("User with id (" + userId + ") doesn't have item with id (" + itemId + ")");
        }
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id (" + userId + ") not found"));
        List<ItemDto> itemDtoList = itemStorage.getUserItems(userId).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
        log.debug("Get user items. Current item counts: {}", itemDtoList.size());
        return itemDtoList;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<ItemDto> itemDtoList = itemStorage.searchItems(text).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
        log.debug("Search items by text: {}. Found {} items", text, itemDtoList.size());
        return itemDtoList;
    }
}

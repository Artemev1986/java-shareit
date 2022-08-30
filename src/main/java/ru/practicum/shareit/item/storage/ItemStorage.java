package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    void addItem(Item item);

    Optional<Item> getItemById(long itemId);

    void updateItem(Item item);

    List<Item> getUserItems(long userId);

    List<Item> searchItems(String text);
}
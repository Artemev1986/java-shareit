package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemStorageInMemory implements ItemStorage {
    private long id;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public void addItem(Item item) {
        id++;
        item.setId(id);
        items.put(id, item);
    }

    @Override
    public Optional<Item> getItemById(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public void updateItem(Item item) {
        items.put(item.getId(), item);
    }

    @Override
    public List<Item> getUserItems(long userId) {
        return items.values().stream().filter(item -> item.getOwnerId() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))
                        || item.getDescription().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))
                        &&  item.getAvailable()).collect(Collectors.toList());
    }
}

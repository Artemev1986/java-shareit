package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> addItem(@RequestHeader(SHARER_USER_ID) Long userId,
                                           @RequestBody @Validated(Create.class) ItemDto itemDto) {
        return new ResponseEntity<>(itemService.addItem(userId, itemDto), HttpStatus.CREATED);
    }

    @PatchMapping("{itemId}")
    public ItemDto updateItem(@PathVariable long itemId,
                              @RequestHeader(SHARER_USER_ID) Long userId,
                              @RequestBody @Valid ItemDto itemDto) {
        return itemService.updateItem(itemId, userId, itemDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getUserItems(@RequestHeader(SHARER_USER_ID) Long userId) {
        List<ItemDto> itemDtoList = itemService.getUserItems(userId);
        if (itemDtoList.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
    }

    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam(name = "text") String text) {
        List<ItemDto> itemDtoList = itemService.searchItems(text);
        if (text.isBlank() || text.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        } else if (itemDtoList.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
        }
    }
}

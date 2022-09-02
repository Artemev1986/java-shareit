package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseSimpleDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<ItemResponseSimpleDto> addItem(@RequestHeader(SHARER_USER_ID) Long userId,
                                                         @RequestBody @Validated(Create.class) ItemRequestDto itemDto) {
        return new ResponseEntity<>(itemService.addItem(userId, itemDto), HttpStatus.CREATED);
    }

    @PatchMapping("{itemId}")
    public ItemResponseSimpleDto updateItem(@PathVariable long itemId,
                                            @RequestHeader(SHARER_USER_ID) Long userId,
                                            @RequestBody @Valid ItemRequestDto itemDto) {
        return itemService.updateItem(itemId, userId, itemDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getUserItems(@RequestHeader(SHARER_USER_ID) Long userId) {
        List<ItemResponseDto> itemDtoList = itemService.getUserItems(userId);
        if (itemDtoList.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
    }

    @GetMapping("{itemId}")
    public ItemResponseDto getItemById(@RequestHeader(SHARER_USER_ID) long userId,
                                       @PathVariable("itemId") long itemId) {
        return itemService.getItemByIdAndUser(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseSimpleDto>> searchItems(@RequestParam(name = "text") String text) {
        List<ItemResponseSimpleDto> itemDtoList = itemService.searchItems(text);
        if (text.isBlank() || text.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        } else if (itemDtoList.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
        }
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(
            @RequestHeader(SHARER_USER_ID) long userId,
            @PathVariable long itemId,
            @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(itemService.addComment(userId, itemId, commentDto), HttpStatus.OK);
    }
}

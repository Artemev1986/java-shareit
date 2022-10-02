package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(SHARER_USER_ID) Long userId,
                                          @RequestBody @Validated(Create.class) ItemRequestDto itemDto) {
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable long itemId,
                                            @RequestHeader(SHARER_USER_ID) Long userId,
                                            @RequestBody @Valid ItemRequestDto itemDto) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(
            @RequestHeader(SHARER_USER_ID) Long userId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "20") int size) {
        ResponseEntity<Object> itemList = itemClient.getUserItems(userId, from, size);

        return itemList;
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(SHARER_USER_ID) long userId,
                                       @PathVariable("itemId") long itemId) {
        return itemClient.getItemByIdAndUser(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestParam(name = "text") String text,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "20") int size) {
        ResponseEntity<Object> itemList = itemClient.searchItems(text, from, size);

            return itemList;

    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader(SHARER_USER_ID) long userId,
            @PathVariable long itemId,
            @Valid @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}

package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.RequestClient;
import ru.practicum.shareit.dto.RequestQueryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestClient requestClient;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(SHARER_USER_ID) Long userId,
                                          @RequestBody @Valid RequestQueryDto requestDto) {
        log.info("Creating request {}, userId={}", requestDto, userId);
        return requestClient.addRequest(requestDto, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @RequestHeader(SHARER_USER_ID) long userId,
            @PathVariable long requestId) {
        log.info("Getting request by userId={} and requestId={}", userId, requestId);
        return requestClient.getRequestByIdAndUserId(requestId, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(SHARER_USER_ID) long userId) {
        log.info("Getting requests by userId = {}", userId);
        return requestClient.getAllByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllByUserId(
            @RequestHeader(SHARER_USER_ID) long userId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "20") int size) {
        log.info("Getting requests by userId = {}", userId);
        return requestClient.getAllByUserId(userId, from, size);
    }
}

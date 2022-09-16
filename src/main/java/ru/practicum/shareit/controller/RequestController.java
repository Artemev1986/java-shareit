package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.RequestQueryDto;
import ru.practicum.shareit.dto.RequestResponseDto;
import ru.practicum.shareit.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService requestService;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<RequestResponseDto> addItem(@RequestHeader(SHARER_USER_ID) Long userId,
                                                      @RequestBody @Valid RequestQueryDto requestDto) {
        return new ResponseEntity<>(requestService.addRequest(requestDto, userId), HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public RequestResponseDto getRequestById(
            @RequestHeader(SHARER_USER_ID) long userId,
            @PathVariable long requestId) {
        return requestService.getRequestByIdAndUserId(requestId, userId);
    }

    @GetMapping()
    public List<RequestResponseDto> getAllByUserId(
            @RequestHeader(SHARER_USER_ID) long userId) {
        return requestService.getAllByUserId(userId);
    }

    @GetMapping("/all")
    public List<RequestResponseDto> getAllByUserId(
            @RequestHeader(SHARER_USER_ID) long userId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "20") int size) {
        return requestService.getAllByUserId(userId, from, size);
    }
}

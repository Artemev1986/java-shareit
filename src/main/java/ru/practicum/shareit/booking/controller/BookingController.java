package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.StateNotValidException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping()
    public ResponseEntity<List<BookingResponseDto>> findAllByUserId(
            @RequestHeader(SHARER_USER_ID) long userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new StateNotValidException("Unknown state: " + state);
        }
        List<BookingResponseDto> bookingResponseDtoList = service.getAllByUserId(userId, state);
        if (bookingResponseDtoList.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookingResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDto>> findAllByOwnerId(
            @RequestHeader(SHARER_USER_ID) long userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new StateNotValidException("Unknown state: " + state);
        }
        List<BookingResponseDto> bookingResponseDtoList = service.getAllByOwnerId(userId, state);
        if (bookingResponseDtoList.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookingResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findById(@RequestHeader(SHARER_USER_ID) long userId, @PathVariable long bookingId) {
        return service.getBookingById(userId, bookingId);
    }

    @PostMapping()
    public ResponseEntity<BookingResponseDto> create(
            @RequestHeader(SHARER_USER_ID) long userId,
            @Valid @RequestBody BookingRequestDto bookingDto) {
        return new ResponseEntity<>(service.addBooking(userId, bookingDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update(
            @RequestHeader(SHARER_USER_ID) long userId,
            @PathVariable long bookingId,
            @RequestParam("approved") boolean approved) {
        return service.updateBooking(userId, bookingId, approved);
    }
}

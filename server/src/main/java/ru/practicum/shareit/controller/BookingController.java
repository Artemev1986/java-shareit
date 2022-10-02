package ru.practicum.shareit.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.BookingRequestDto;
import ru.practicum.shareit.dto.BookingResponseDto;
import ru.practicum.shareit.service.BookingService;

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
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        List<BookingResponseDto> bookingResponseDtoList = service.getAllByUserId(userId, state, from, size);
        if (bookingResponseDtoList.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookingResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDto>> findAllByOwnerId(
            @RequestHeader(SHARER_USER_ID) long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        List<BookingResponseDto> bookingResponseDtoList = service.getAllByOwnerId(userId, state, from, size);
        if (bookingResponseDtoList.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookingResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findById(@RequestHeader(SHARER_USER_ID) long userId, @PathVariable long bookingId) {
        return service.getBookingByIdAndUserId(userId, bookingId);
    }

    @PostMapping()
    public ResponseEntity<BookingResponseDto> addBooking(
            @RequestHeader(SHARER_USER_ID) long userId,
            @RequestBody BookingRequestDto bookingDto) {
        return new ResponseEntity<>(service.addBooking(userId, bookingDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBooking(
            @RequestHeader(SHARER_USER_ID) long userId,
            @PathVariable long bookingId,
            @RequestParam("approved") boolean approved) {
        return service.updateBooking(userId, bookingId, approved);
    }
}

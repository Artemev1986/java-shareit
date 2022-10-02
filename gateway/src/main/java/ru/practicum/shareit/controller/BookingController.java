package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.BookingClient;
import ru.practicum.shareit.dto.BookingRequestDto;
import ru.practicum.shareit.exception.StateNotValidException;
import ru.practicum.shareit.dto.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;
	private static final String SHARER_USER_ID = "X-Sharer-User-Id";

	@GetMapping
	public ResponseEntity<Object> findAllByUserId(@RequestHeader(SHARER_USER_ID) long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		State state = State.from(stateParam)
				.orElseThrow(() -> new StateNotValidException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.findAllByUserId(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> findAllByOwnerId(
			@RequestHeader(SHARER_USER_ID) long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		State state = State.from(stateParam)
				.orElseThrow(() -> new StateNotValidException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.findAllByOwnerId(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestHeader(SHARER_USER_ID) long userId,
			@RequestBody @Valid BookingRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.addBooking(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(SHARER_USER_ID) long userId,
			@PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateBooking(
			@RequestHeader(SHARER_USER_ID) long userId,
			@Positive @PathVariable long bookingId,
			@RequestParam("approved") boolean approved) {
		log.info("Updating booking {}, userId={}", bookingId, userId);
		return bookingClient.updateBooking(userId, bookingId, approved);
	}
}

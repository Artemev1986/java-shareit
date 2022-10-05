package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.BookingRequestDto;
import ru.practicum.shareit.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto addBooking(long userId, BookingRequestDto bookingDto);

    BookingResponseDto updateBooking(long userId, long bookingId, boolean approved);

    BookingResponseDto getBookingByIdAndUserId(long userId, long bookingId);

    List<BookingResponseDto> getAllByUserId(long userId, String state, int from, int size);

    List<BookingResponseDto> getAllByOwnerId(long userId, String state, int from, int size);
}

package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    List<BookingResponseDto> getAllByUserId(long userId, String state);

    List<BookingResponseDto> getAllByOwnerId(long userId, String state);

    BookingResponseDto getBookingById(long userId, long bookingId);

    BookingResponseDto addBooking(long userId, BookingRequestDto bookingDto);

    BookingResponseDto updateBooking(long userId, long bookingId, boolean approved);
}

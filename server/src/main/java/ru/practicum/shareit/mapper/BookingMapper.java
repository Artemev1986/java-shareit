package ru.practicum.shareit.mapper;

import lombok.NonNull;
import ru.practicum.shareit.dto.BookingRequestDto;
import ru.practicum.shareit.dto.BookingResponseDto;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;

public class BookingMapper {
    public static BookingResponseDto toBookingDto(@NonNull Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setItem(ItemMapper.toItemDto(booking.getItem()));
        bookingResponseDto.setBooker(booking.getBooker());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setStatus(booking.getStatus());
        return bookingResponseDto;
    }

    public static Booking toBooking(BookingRequestDto bookingDto, Item item, User user) {
        if (bookingDto == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }
}

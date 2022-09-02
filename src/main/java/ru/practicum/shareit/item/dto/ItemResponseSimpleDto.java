package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemResponseSimpleDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;

    public static class BookingDto {
        final Long id;
        final Long bookerId;

        public BookingDto(Long id, Long bookerId) {
            this.id = id;
            this.bookerId = bookerId;
        }
    }
}

package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingResponseDtoForItem;

import java.util.List;

@Data
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingResponseDtoForItem lastBooking;
    private BookingResponseDtoForItem nextBooking;
    private List<CommentDto> comments;
}

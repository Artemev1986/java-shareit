package ru.practicum.shareit.dto;

import lombok.Data;

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

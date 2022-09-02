package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemResponseSimpleDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private long id;
    private ItemResponseSimpleDto item;
    private User booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
}

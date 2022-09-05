package ru.practicum.shareit.dto;

import lombok.Data;
import ru.practicum.shareit.model.Status;
import ru.practicum.shareit.model.User;

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

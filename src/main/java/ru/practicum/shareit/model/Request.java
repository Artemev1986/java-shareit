package ru.practicum.shareit.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Request {
    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
}

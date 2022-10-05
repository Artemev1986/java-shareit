package ru.practicum.shareit.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemResponseSimpleDto> items;
}

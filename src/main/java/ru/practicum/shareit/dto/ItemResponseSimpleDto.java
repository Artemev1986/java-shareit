package ru.practicum.shareit.dto;

import lombok.Data;

@Data
public class ItemResponseSimpleDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}

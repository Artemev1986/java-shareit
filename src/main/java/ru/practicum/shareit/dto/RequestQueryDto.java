package ru.practicum.shareit.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RequestQueryDto {
    @NotBlank(message = "{description.notblank}")
    private String description;
}

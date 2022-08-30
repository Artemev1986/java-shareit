package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {
    private Long id;
    @NotBlank(message = "{name.notblank}", groups = {Create.class})
    private String name;
    @NotBlank(message = "{description.notblank}", groups = {Create.class})
    private String description;
    @NotNull(message = "{available.notnull}", groups = {Create.class})
    private Boolean available;
}

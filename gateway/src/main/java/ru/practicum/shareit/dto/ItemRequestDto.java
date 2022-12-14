package ru.practicum.shareit.dto;

import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "{name.notblank}", groups = {Create.class})
    private String name;
    @NotBlank(message = "{description.notblank}", groups = {Create.class})
    private String description;
    @NotNull(message = "{available.notnull}", groups = {Create.class})
    private Boolean available;
    private Long requestId;
}

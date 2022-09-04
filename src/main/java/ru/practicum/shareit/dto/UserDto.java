package ru.practicum.shareit.dto;

import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {
    private Long id;
    @NotBlank(message = "{name.notblank}", groups = {Create.class})
    private String name;
    @NotEmpty(message = "{email.notempty}", groups = {Create.class})
    @Email(message = "{email.valid}", groups = {Create.class})
    private String email;
}

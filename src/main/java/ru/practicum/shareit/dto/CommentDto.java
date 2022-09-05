package ru.practicum.shareit.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private long id;
    private String authorName;
    @NotBlank(message = "{text.notblank}")
    private String text;
    private LocalDateTime created;
}

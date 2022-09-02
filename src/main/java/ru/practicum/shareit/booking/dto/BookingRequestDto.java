package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    @NotNull(message = "{itemId.notnull}")
    private Long itemId;
    @NotNull(message = "{start.notnull}")
    @FutureOrPresent(message = "{start.futureOrPresent}")
    private LocalDateTime start;
    @NotNull(message = "{end.notnull}")
    @Future(message = "{end.future}")
    private LocalDateTime end;
}

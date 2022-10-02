package ru.practicum.shareit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingResponseDtoForItem {
    Long id;
    Long bookerId;
}

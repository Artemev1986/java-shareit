package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.RequestQueryDto;
import ru.practicum.shareit.dto.RequestResponseDto;

import java.util.List;

public interface RequestService {
    RequestResponseDto addRequest(RequestQueryDto requestDto, long requestorId);

    RequestResponseDto getRequestByIdAndUserId(long requestId, long requestorId);

    List<RequestResponseDto> getAllByUserId(long userId);

    List<RequestResponseDto> getAllByUserId(long userId, int from, int size);
}

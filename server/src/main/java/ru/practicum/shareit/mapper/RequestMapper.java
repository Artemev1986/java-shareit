package ru.practicum.shareit.mapper;

import ru.practicum.shareit.dto.ItemResponseSimpleDto;
import ru.practicum.shareit.dto.RequestQueryDto;
import ru.practicum.shareit.dto.RequestResponseDto;
import ru.practicum.shareit.model.Request;
import ru.practicum.shareit.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class RequestMapper {
    public static RequestResponseDto toRequestDto(Request request, List<ItemResponseSimpleDto> items) {
        RequestResponseDto requestDto = new RequestResponseDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        requestDto.setItems(items);
        return requestDto;
    }

    public static Request toRequest(RequestQueryDto requestDto, User requestor) {
        Request request = new Request();
        request.setDescription(requestDto.getDescription());
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());
        return request;
    }
}

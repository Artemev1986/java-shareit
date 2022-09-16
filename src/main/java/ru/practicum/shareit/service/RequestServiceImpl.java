package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.RequestQueryDto;
import ru.practicum.shareit.dto.RequestResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.mapper.RequestMapper;
import ru.practicum.shareit.model.Request;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.RequestRepository;
import ru.practicum.shareit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public RequestResponseDto addRequest(RequestQueryDto requestDto, long requestorId) {
        Request request = RequestMapper.toRequest(requestDto, getUserById(requestorId));
        Request requestResponse = requestRepository.save(request);
        log.debug("Adding new request with id: {}", request.getId());
        return RequestMapper.toRequestDto(requestResponse, new ArrayList<>());
    }

    @Override
    public RequestResponseDto getRequestByIdAndUserId(long requestId, long requestorId) {
        getUserById(requestorId);
        Request request = getRequestById(requestId);
        RequestResponseDto requestDto = RequestMapper.toRequestDto(
                request,
                itemRepository.findAllByRequestId(request.getId()).stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList()));
        log.debug("Get request by id: {}", request.getId());
        return requestDto;
    }

    @Override
    public List<RequestResponseDto> getAllByUserId(long userId) {
        User user = getUserById(userId);
        List<RequestResponseDto> requestList = requestRepository.findAllByRequestorOrderByCreatedDesc(user).stream()
                .map(request -> RequestMapper.toRequestDto(
                        request,
                        itemRepository.findAllByRequestId(request.getId()).stream()
                                .map(ItemMapper::toItemDto)
                                .collect(Collectors.toList()))).collect(Collectors.toList());
        log.debug("Get all request by user with id: {}", userId);
        return requestList;
    }

    @Override
    public List<RequestResponseDto> getAllByUserId(long userId, int from, int size) {
        User user = getUserById(userId);
        Pageable page = PageRequest.of(from / size, size, Sort.by("created").ascending());
        List<RequestResponseDto> requestList = requestRepository.findAllByRequestorNotOrderByCreatedDesc(user, page).stream()
                .map(request -> RequestMapper.toRequestDto(
                        request,
                        itemRepository.findAllByRequestId(request.getId()).stream()
                                .map(ItemMapper::toItemDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
                log.debug("Get all request by user with id: {}", userId);
        return requestList;
    }

    private User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id (" + id + ") not found"));
    }

    private Request getRequestById(long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id (" + requestId + ") not found"));
    }
}

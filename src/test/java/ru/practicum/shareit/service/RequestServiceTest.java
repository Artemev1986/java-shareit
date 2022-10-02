package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.dto.ItemResponseSimpleDto;
import ru.practicum.shareit.dto.RequestQueryDto;
import ru.practicum.shareit.dto.RequestResponseDto;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.mapper.RequestMapper;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.Request;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.RequestRepository;
import ru.practicum.shareit.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceTest {

    private final RequestService requestService;

    @MockBean
    private final RequestRepository requestRepository;

    @MockBean
    private final UserRepository userRepository;

    @MockBean
    private final ItemRepository itemRepository;

    private final User user = new User();
    private final Item item = new Item();
    private final Request request = new Request();
    private final RequestQueryDto requestDto = new RequestQueryDto();
    private RequestResponseDto requestResponseDto;

    @BeforeEach
    void beforeEach() {
        user.setId(1L);
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");

        item.setId(1L);
        item.setName("Item1");
        item.setDescription("Item1 description1");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(1L);

        request.setId(1L);
        request.setDescription("Request description");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());

        requestDto.setDescription("Request description");

    }

    @Test
    void addRequest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(requestRepository.save(any()))
                .thenReturn(request);

        requestResponseDto = requestService.addRequest(requestDto, user.getId());

        assertThat(requestResponseDto).isEqualTo(RequestMapper.toRequestDto(request, new ArrayList<>()));
    }

    @Test
    void getRequestByIdAndUserId() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(request));

        List<Item> items = List.of(item);
        Mockito.when(itemRepository.findAllByRequestId(anyLong()))
                .thenReturn(items);

        requestResponseDto = requestService.getRequestByIdAndUserId(request.getId(), user.getId());

        assertThat(requestResponseDto)
                .isEqualTo(RequestMapper
                        .toRequestDto(request, items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList())));
    }

    @Test
    void getAllByUserId() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        List<Request> requests = List.of(request);

        List<ItemResponseSimpleDto> itemsResponseDto = List.of(ItemMapper.toItemDto(item));
        List<RequestResponseDto> requestsResponseDto = requests.stream()
                .map(request -> RequestMapper.toRequestDto(request,
                        itemsResponseDto)).collect(Collectors.toList());
        Mockito
                .when(requestRepository.findAllByRequestorOrderByCreatedDesc(any()))
                .thenReturn(requests);

        List<Item> items = List.of(item);
        Mockito
                .when(itemRepository.findAllByRequestId(anyLong()))
                .thenReturn(items);

        List<RequestResponseDto> requestsDto = requestService.getAllByUserId(user.getId());

        assertThat(requestsDto).isEqualTo(requestsResponseDto);
    }

    @Test
    void getAllByUserIdPage() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        List<Request> requests = List.of(request);

        List<ItemResponseSimpleDto> itemsResponseDto = List.of(ItemMapper.toItemDto(item));
        List<RequestResponseDto> requestsResponseDto = requests.stream()
                .map(request -> RequestMapper.toRequestDto(request,
                        itemsResponseDto)).collect(Collectors.toList());
        Mockito
                .when(requestRepository.findAllByRequestorNotOrderByCreatedDesc(any(), any()))
                .thenReturn(requests);

        List<Item> items = List.of(item);
        Mockito
                .when(itemRepository.findAllByRequestId(anyLong()))
                .thenReturn(items);

        List<RequestResponseDto> requestsDto = requestService.getAllByUserId(user.getId(), 0, 10);

        assertThat(requestsDto).isEqualTo(requestsResponseDto);
    }
}
package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.dto.RequestQueryDto;
import ru.practicum.shareit.dto.RequestResponseDto;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.mapper.RequestMapper;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.Request;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RequestService requestService;

    private final RequestQueryDto requestQueryDto = new RequestQueryDto();
    private final User user = new User();
    private final Item item = new Item();
    private final Request request = new Request();
    private RequestResponseDto requestResponseDto;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @BeforeEach
    void BeforeEach() {
        user.setId(1L);
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");

        item.setId(1L);
        item.setName("Item1");
        item.setDescription("Item1 description1");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(1L);

        requestQueryDto.setDescription("Request description");

        request.setId(1L);
        request.setDescription("Request description");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.of(2022, 9, 20, 1, 1, 1));

        requestResponseDto = RequestMapper.toRequestDto(request, List.of(ItemMapper.toItemDto(item)));
    }

    @Test
    void addItem() throws Exception {
        Mockito
                .when(requestService.addRequest(any(), anyLong()))
                .thenReturn(requestResponseDto);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestQueryDto))
                        .header(SHARER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestResponseDto.getId()))
                .andExpect(jsonPath("$.description").value(requestResponseDto.getDescription()))
                .andExpect(jsonPath("$.created").value(requestResponseDto.getCreated().toString()));
    }

    @Test
    void getRequestById() throws Exception {
        Mockito
                .when(requestService.getRequestByIdAndUserId(anyLong(), anyLong()))
                .thenReturn(requestResponseDto);

        mockMvc.perform(get("/requests/1")
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestResponseDto.getId()))
                .andExpect(jsonPath("$.description").value(requestResponseDto.getDescription()))
                .andExpect(jsonPath("$.created").value(requestResponseDto.getCreated().toString()));
    }

    @Test
    void getAllByUserId() throws Exception {
        Mockito
                .when(requestService.getAllByUserId(anyLong()))
                .thenReturn(List.of(requestResponseDto));

        mockMvc.perform(get("/requests")
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(requestResponseDto))));
    }

    @Test
    void GetAllByUserIdPage() throws Exception {
        Mockito
                .when(requestService.getAllByUserId(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(requestResponseDto));

        mockMvc.perform(get("/requests/all")
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(requestResponseDto))));
    }
}
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
import ru.practicum.shareit.dto.BookingRequestDto;
import ru.practicum.shareit.dto.BookingResponseDto;
import ru.practicum.shareit.dto.ItemResponseSimpleDto;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.Status;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BookingService bookingService;

    private final User user = new User();
    private final Item item = new Item();

    private final BookingResponseDto bookingResponseDto = new BookingResponseDto();
    private final BookingRequestDto bookingRequestDto = new BookingRequestDto();
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @BeforeEach
    void BeforeEach() {
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");

        item.setName("Item1");
        item.setDescription("Item1 description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(null);

        ItemResponseSimpleDto itemDto = ItemMapper.toItemDto(item);

        bookingResponseDto.setId(1L);
        LocalDateTime start = LocalDateTime.of(2022, 9, 20, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2022, 9, 21, 1, 2, 1);
        bookingResponseDto.setStart(start);
        bookingResponseDto.setEnd(end);
        bookingResponseDto.setItem(itemDto);
        bookingResponseDto.setBooker(user);
        bookingResponseDto.setStatus(Status.WAITING);

        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(start);
        bookingRequestDto.setEnd(end);
    }

    @Test
    void findAllByUserId() throws Exception {
        Mockito
                .when(bookingService.getAllByUserId(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));

        mockMvc.perform(get("/bookings")
                        .header(SHARER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingResponseDto))));
    }

    @Test
    void findAllByOwnerId() throws Exception {
        Mockito
                .when(bookingService.getAllByOwnerId(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));

        mockMvc.perform(get("/bookings/owner")
                        .header(SHARER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingResponseDto))));
    }

    @Test
    void findById() throws Exception {
        Mockito
                .when(bookingService.getBookingByIdAndUserId(anyLong(), anyLong()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(get("/bookings/1")
                        .header(SHARER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()))
                .andExpect(jsonPath("$.start").value(bookingResponseDto.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingResponseDto.getEnd().toString()))
                .andExpect(jsonPath("$.status").value(bookingResponseDto.getStatus().toString()));
    }

    @Test
    void create() throws Exception {
        Mockito
                .when(bookingService.addBooking(anyLong(), any()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(post("/bookings")
                        .header(SHARER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()))
                .andExpect(jsonPath("$.start").value(bookingResponseDto.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingResponseDto.getEnd().toString()))
                .andExpect(jsonPath("$.status").value(bookingResponseDto.getStatus().toString()))
                .andExpect(status().isCreated());
    }

    @Test
    void update() throws Exception {
        Mockito
                .when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(patch("/bookings/1")
                        .header(SHARER_USER_ID, 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()))
                .andExpect(jsonPath("$.start").value(bookingResponseDto.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingResponseDto.getEnd().toString()))
                .andExpect(jsonPath("$.status").value(bookingResponseDto.getStatus().toString()));
    }
}
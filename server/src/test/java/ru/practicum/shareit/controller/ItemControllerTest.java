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
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemRequestDto;
import ru.practicum.shareit.dto.ItemResponseDto;
import ru.practicum.shareit.dto.ItemResponseSimpleDto;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.model.*;
import ru.practicum.shareit.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    private final User user = new User();
    private final Item item = new Item();
    private ItemResponseSimpleDto itemDto;
    private final ItemRequestDto itemRequestDto = new ItemRequestDto();
    private ItemResponseDto itemResponseDto = new ItemResponseDto();
    private final Comment comment = new Comment();
    private final Item itemLast = new Item();
    private final Item itemNext = new Item();
    private final Booking bookingLast = new Booking();
    private final Booking bookingNext = new Booking();

    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

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
        item.setRequestId(null);

        itemDto = ItemMapper.toItemDto(item);

        itemRequestDto.setId(1L);
        itemRequestDto.setName("Item1");
        itemRequestDto.setDescription("Item1 description1");
        itemRequestDto.setAvailable(true);
        itemRequestDto.setRequestId(null);

        comment.setId(1L);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setText("comment");
        comment.setCreated(LocalDateTime.of(2022, 9, 20, 1, 2, 3));

        itemLast.setId(2L);
        itemLast.setName("ItemLast");
        itemLast.setDescription("Item description");
        itemLast.setAvailable(true);
        itemLast.setOwner(user);
        itemLast.setRequestId(null);

        itemNext.setId(3L);
        itemNext.setName("ItemNext");
        itemNext.setDescription("Item description");
        itemNext.setAvailable(true);
        itemNext.setOwner(user);
        itemNext.setRequestId(null);

        LocalDateTime start = LocalDateTime.of(2022, 9, 20, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2022, 9, 21, 1, 2, 1);

        bookingLast.setId(1L);
        bookingLast.setStart(start.minusDays(1));
        bookingLast.setEnd(end.minusDays(1));
        bookingLast.setItem(itemLast);
        bookingLast.setBooker(user);
        bookingLast.setStatus(Status.APPROVED);

        bookingNext.setId(2L);
        bookingNext.setStart(start.plusDays(1));
        bookingNext.setEnd(end.plusDays(1));
        bookingNext.setItem(itemNext);
        bookingNext.setBooker(user);
        bookingNext.setStatus(Status.WAITING);

        itemResponseDto = ItemMapper
                .toItemBookingDto(item, bookingLast, bookingNext, List.of(CommentMapper.toCommentDto(comment)));
    }

    @Test
    void addItem() throws Exception {
        Mockito
                .when(itemService.addItem(anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(SHARER_USER_ID, 1)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()));
    }

    @Test
    void updateItem() throws Exception {
        Mockito
                .when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header(SHARER_USER_ID, 1)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()));
    }

    @Test
    void getUserItems() throws Exception {
        Mockito
                .when(itemService.getUserItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemResponseDto));

        mockMvc.perform(get("/items")
                        .header(SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemResponseDto))));
    }

    @Test
    void getItemById() throws Exception {
        Mockito
                .when(itemService.getItemByIdAndUser(anyLong(), anyLong()))
                .thenReturn(itemResponseDto);

        mockMvc.perform(get("/items/1")
                        .header(SHARER_USER_ID, 1)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(itemResponseDto.getName()))
                .andExpect(jsonPath("$.description").value(itemResponseDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemResponseDto.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemResponseDto.getRequestId()));
    }

    @Test
    void searchItems() throws Exception {
        Mockito
                .when(itemService.searchItems(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .header(SHARER_USER_ID, 1)
                        .param("text", "Item1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        Mockito
                .when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header(SHARER_USER_ID, 1)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.created").value(commentDto.getCreated().toString()));
    }
}
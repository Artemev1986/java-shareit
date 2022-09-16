package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemRequestDto;
import ru.practicum.shareit.dto.ItemResponseDto;
import ru.practicum.shareit.dto.ItemResponseSimpleDto;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.model.*;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.CommentRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {

    private final ItemService itemService;

    @MockBean
    private final BookingRepository bookingRepository;

    @MockBean
    private final UserRepository userRepository;

    @MockBean
    private final ItemRepository itemRepository;

    @MockBean
    private final CommentRepository commentRepository;

    private final User user = new User();
    private final Item item = new Item();
    private final Comment comment = new Comment();
    private final Item itemLast = new Item();
    private final Item itemNext = new Item();
    private final Booking bookingLast = new Booking();
    private final Booking bookingNext = new Booking();
    private final ItemRequestDto itemRequestDto = new ItemRequestDto();
    private ItemResponseSimpleDto itemSimpleDto;

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

        itemRequestDto.setId(1L);
        itemRequestDto.setName("Item1");
        itemRequestDto.setDescription("Item1 description1");
        itemRequestDto.setAvailable(true);
        itemRequestDto.setRequestId(null);

        comment.setId(1L);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setText("comment");
        comment.setCreated(LocalDateTime.now());

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

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

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
    }

    @Test
    void addItem() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.save(any()))
                .thenReturn(item);

        itemSimpleDto = itemService.addItem(user.getId(), itemRequestDto);

        assertThat(itemSimpleDto).isEqualTo(ItemMapper.toItemDto(item));
    }

    @Test
    void getItemByIdAndUser() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(bookingRepository
                        .findFirstByItemOwnerAndStartBeforeAndStatusOrderByStartDesc(any(), any(), any()))
                .thenReturn(bookingLast);
        Mockito
                .when(bookingRepository
                        .findFirstByItemOwnerAndStartAfterAndStatusOrderByStartAsc(any(), any(), any()))
                .thenReturn(bookingNext);
        Mockito
                .when(commentRepository.findAllByItemOrderByCreated(item))
                .thenReturn(List.of(comment));

        ItemResponseDto itemResponseDto = itemService.getItemByIdAndUser(user.getId(), item.getId());

        ItemResponseDto itemDto = ItemMapper
                .toItemBookingDto(item, bookingLast, bookingNext, List.of(CommentMapper.toCommentDto(comment)));

        assertThat(itemResponseDto).isEqualTo(itemDto);

    }

    @Test
    void updateItem() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(itemRepository.save(any()))
                .thenReturn(item);

        itemSimpleDto = itemService.updateItem(item.getId(), user.getId(), itemRequestDto);

        assertThat(itemSimpleDto).isEqualTo(ItemMapper.toItemDto(item));
    }

    @Test
    void getUserItems() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findAllByOwnerOrderByIdAsc(any(), any()))
                .thenReturn(List.of(item));
        Mockito
                .when(bookingRepository
                        .findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(any(), any(), any()))
                .thenReturn(bookingLast);
        Mockito
                .when(bookingRepository
                        .findFirstByItemAndStartAfterAndStatusOrderByStartAsc(any(), any(), any()))
                .thenReturn(bookingNext);
        Mockito
                .when(commentRepository.findAllByItemOrderByCreated(item))
                .thenReturn(List.of(comment));

        List<ItemResponseDto> items = itemService.getUserItems(user.getId(), 0, 10);

        ItemResponseDto itemDto = ItemMapper
                .toItemBookingDto(item, bookingLast, bookingNext, List.of(CommentMapper.toCommentDto(comment)));

        assertThat(items).isEqualTo(List.of(itemDto));
    }

    @Test
    void searchItems() {
        Mockito
                .when(itemRepository.findByText(any(), any()))
                .thenReturn(List.of(item));
        List<ItemResponseSimpleDto> items = itemService.searchItems("Item1", 0, 10);

        assertThat(items).isEqualTo(List.of(ItemMapper.toItemDto(item)));
    }

    @Test
    void addComment() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(bookingRepository
                        .findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(any(), any(), any(), any()))
                .thenReturn(new Booking());

        Comment commentResponse = CommentMapper.toComment(CommentMapper.toCommentDto(comment), item, user);
        Mockito
                .when(commentRepository.save(any()))
                .thenReturn(commentResponse);

        CommentDto commentDto = itemService.addComment(user.getId(), item.getId(), CommentMapper.toCommentDto(comment));

        assertThat(commentDto).isEqualTo(CommentMapper.toCommentDto(commentResponse));
    }
}
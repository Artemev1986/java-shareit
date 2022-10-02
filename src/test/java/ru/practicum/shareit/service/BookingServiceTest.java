package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.dto.BookingRequestDto;
import ru.practicum.shareit.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.model.*;
import ru.practicum.shareit.repository.BookingRepository;
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
class BookingServiceTest {

    private final BookingService bookingService;

    @MockBean
    private final BookingRepository bookingRepository;

    @MockBean
    private final UserRepository userRepository;

    @MockBean
    private final ItemRepository itemRepository;

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto();
    private BookingResponseDto bookingResponseDto;

    LocalDateTime start = LocalDateTime.now();
    LocalDateTime end = LocalDateTime.now().plusDays(1);
    private final User owner = new User();
    private final User booker = new User();
    private final Item item = new Item();
    private final Booking booking = new Booking();
    private List<BookingResponseDto> bookings;

    @BeforeEach
    void beforeEach() {
        owner.setId(1L);
        owner.setName("Mikhail");
        owner.setEmail("Mikhail@gmail.com");

        booker.setId(2L);
        booker.setName("Andrey");
        booker.setEmail("Andrey@gmail.com");

        item.setId(1L);
        item.setName("Item1");
        item.setDescription("Item1 description");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequestId(null);

        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(start);
        bookingRequestDto.setEnd(end);

        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);
    }

    @Test
    void addBooking() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);

        bookingResponseDto = bookingService.addBooking(booker.getId(), bookingRequestDto);

        assertThat(bookingResponseDto).isEqualTo(BookingMapper.toBookingDto(booking));

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.addBooking(owner.getId(), bookingRequestDto));
    }

    @Test
    void updateBooking() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);

        bookingResponseDto = bookingService.updateBooking(owner.getId(), booking.getId(),true);

        assertThat(bookingResponseDto.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    void getBookingByIdAndUserId() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        bookingResponseDto = bookingService.getBookingByIdAndUserId(booker.getId(), booking.getId());

        assertThat(bookingResponseDto).isEqualTo(BookingMapper.toBookingDto(booking));
    }

    @Test
    void getAllByUserId() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));
        Mockito
                .when(bookingRepository.findAllByBookerAndStatusOrderByStartAsc(any(), any(), any()))
                .thenReturn(List.of(booking));

        bookings = bookingService.getAllByUserId(booker.getId(), State.WAITING.toString(), 0, 10);

        assertThat(bookings).isEqualTo(List.of(BookingMapper.toBookingDto(booking)));
    }

    @Test
    void getAllByOwnerId() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(owner));
        Mockito
                .when(bookingRepository.findAllByItemOwnerAndStatusOrderByStartAsc(any(), any(), any()))
                .thenReturn(List.of(booking));

        bookings = bookingService.getAllByOwnerId(owner.getId(), State.WAITING.toString(), 0, 10);

        assertThat(bookings).isEqualTo(List.of(BookingMapper.toBookingDto(booking)));
    }
}
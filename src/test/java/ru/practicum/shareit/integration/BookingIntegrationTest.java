package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.dto.BookingRequestDto;
import ru.practicum.shareit.dto.BookingResponseDto;
import ru.practicum.shareit.model.*;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingIntegrationTest {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingService bookingService;

    private final User owner = new User();
    private final User booker = new User();
    private final Item item = new Item();
    private final Booking booking = new Booking();

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto();
    LocalDateTime start = LocalDateTime.of(2022, 9, 20, 1, 1, 1);
    LocalDateTime end = LocalDateTime.of(2022, 9, 21, 1, 1, 1);

    @Test
    void testBooking() {
        owner.setName("Mikhail");
        owner.setEmail("test@gmail.com");
        userRepository.save(owner);

        booker.setName("Oleg");
        booker.setEmail("oleg@gmail.com");

        userRepository.save(booker);

        item.setName("Item1");
        item.setDescription("Item1 description");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequestId(null);

        itemRepository.save(item);

        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);

        bookingRequestDto.setItemId(item.getId());
        bookingRequestDto.setStart(start);
        bookingRequestDto.setEnd(end);


        bookingService.addBooking(booker.getId(), bookingRequestDto);

        List<BookingResponseDto> bookings = bookingService
                .getAllByUserId(booker.getId(), State.WAITING.toString(), 0, 10);

        AssertionsForClassTypes.assertThat(bookings.size()).isEqualTo(1);
    }
}

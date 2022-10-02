package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.Status;
import ru.practicum.shareit.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
class BookingRepositoryTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private List<Booking> bookings;
    private Booking bookingResponse;
    private final User user = new User();
    private final Item item = new Item();
    Item itemLast = new Item();
    Item itemNext = new Item();

    private final Booking booking = new Booking();
    Booking bookingLast = new Booking();
    Booking bookingNext = new Booking();

    private final Pageable page = PageRequest.of(0, 10);

    @BeforeEach
    void beforeEach() {
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");

        item.setName("Item1");
        item.setDescription("Item1 description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(null);

        itemLast.setName("ItemLast");
        itemLast.setDescription("Item description");
        itemLast.setAvailable(true);
        itemLast.setOwner(user);
        itemLast.setRequestId(null);

        itemNext.setName("ItemNext");
        itemNext.setDescription("Item description");
        itemNext.setAvailable(true);
        itemNext.setOwner(user);
        itemNext.setRequestId(null);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);

        bookingLast.setStart(start.minusDays(1));
        bookingLast.setEnd(end.minusDays(1));
        bookingLast.setItem(itemLast);
        bookingLast.setBooker(user);
        bookingLast.setStatus(Status.APPROVED);

        bookingNext.setStart(start.plusDays(1));
        bookingNext.setEnd(end.plusDays(1));
        bookingNext.setItem(itemNext);
        bookingNext.setBooker(user);
        bookingNext.setStatus(Status.WAITING);

        entityManager.persist(user);
        entityManager.persist(item);
        entityManager.persist(itemLast);
        entityManager.persist(itemNext);
        entityManager.persist(booking);
        entityManager.persist(bookingLast);
        entityManager.persist(bookingNext);
    }

    @Test
    void findFirstByItemAndBookerAndStartBeforeAndStatus() {
        bookingResponse = bookingRepository
                .findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(
                        item,
                        user,
                        LocalDateTime.now(),
                        Status.WAITING);

        assertThat(bookingResponse).isEqualTo(booking);
    }

    @Test
    void findAllByBooker() {
        bookings = bookingRepository
                .findAllByBookerOrderByStartDesc(user, page);

        assertThat(bookings).isEqualTo(List.of(bookingNext, booking, bookingLast));
    }

    @Test
    void findAllByBookerAndStatus() {
        bookings = bookingRepository
                .findAllByBookerAndStatusOrderByStartAsc(user, Status.WAITING, page);

        assertThat(bookings).isEqualTo(List.of(booking, bookingNext));
    }

    @Test
    void findAllByBookerAndStartAfter() {
        bookings = bookingRepository
                .findAllByBookerAndStartAfterOrderByStartDesc(user, LocalDateTime.now(), page);
        assertThat(bookings).isEqualTo(List.of(bookingNext));
    }

    @Test
    void findAllByBookerAndEndBefore() {
        bookings = bookingRepository
                .findAllByBookerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now(), page);

        assertThat(bookings).isEqualTo(List.of(bookingLast));
    }

    @Test
    void findAllByBookerAndStartBeforeAndEndAfter() {
        bookings = bookingRepository
                .findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
                        user,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        page);

        assertThat(bookings).isEqualTo(List.of(booking));
    }

    @Test
    void findAllByItemOwner() {
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerOrderByStartDesc(user, page);

        assertThat(bookings).isEqualTo(List.of(bookingNext, booking, bookingLast));
    }

    @Test
    void findAllByItemOwnerAndStatus() {
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerAndStatusOrderByStartAsc(user, Status.WAITING, page);

        assertThat(bookings).isEqualTo(List.of(booking, bookingNext));
    }

    @Test
    void findAllByItemOwnerAndStartBeforeAndEndAfter() {
        bookings = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
                user,
                LocalDateTime.now(),
                LocalDateTime.now(),
                page
        );

        assertThat(bookings).isEqualTo(List.of(booking));
    }

    @Test
    void findAllByItemOwnerAndEndBefore() {
        bookings = bookingRepository
                .findAllByItemOwnerAndEndBeforeOrderByStartDesc(
                        user,
                        LocalDateTime.now(),
                        page);

        assertThat(bookings).isEqualTo(List.of(bookingLast));
    }

    @Test
    void findAllByItemOwnerAndStartAfter() {
        bookings = bookingRepository
                .findAllByItemOwnerAndStartAfterOrderByStartDesc(
                        user,
                        LocalDateTime.now(),
                        page);

        assertThat(bookings).isEqualTo(List.of(bookingNext));
    }

    @Test
    void findFirstByItemOwnerAndStartBeforeAndStatus() {
        bookingResponse = bookingRepository.findFirstByItemOwnerAndStartBeforeAndStatusOrderByStartDesc(
                user, LocalDateTime.now(), Status.WAITING);

        assertThat(bookingResponse).isEqualTo(booking);
    }

    @Test
    void findFirstByItemOwnerAndStartAfterAndStatus() {
        bookingResponse = bookingRepository.findFirstByItemOwnerAndStartAfterAndStatusOrderByStartAsc(
                user, LocalDateTime.now(), Status.WAITING);

        assertThat(bookingResponse).isEqualTo(bookingNext);
    }

    @Test
    void findFirstByItemAndStartBeforeAndStatus() {
        bookingResponse = bookingRepository.findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
                item, LocalDateTime.now(), Status.WAITING);

        assertThat(bookingResponse).isEqualTo(booking);
    }

    @Test
    void findFirstByItemAndStartAfterAndStatus() {
        bookingResponse = bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
                item, LocalDateTime.now(), Status.WAITING);

        assertThat(bookingResponse).isEqualTo(null);
    }
}
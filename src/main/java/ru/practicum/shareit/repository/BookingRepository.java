package ru.practicum.shareit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.Status;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(
            Item item, User booker, LocalDateTime date, Status status);

    List<Booking> findAllByBookerOrderByStartDesc(
            User booker, Pageable page);

    List<Booking> findAllByBookerAndStatusOrderByStartAsc(
            User booker, Status status, Pageable page);

    List<Booking> findAllByBookerAndStartAfterOrderByStartDesc(
            User booker, LocalDateTime date, Pageable page);

    List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(
            User booker, LocalDateTime date, Pageable page);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
            User booker, LocalDateTime start, LocalDateTime end, Pageable page);

    List<Booking> findAllByItemOwnerOrderByStartDesc(User owner, Pageable page);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartAsc(
            User owner, Status status, Pageable page);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
            User owner, LocalDateTime start, LocalDateTime end, Pageable page);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(
            User owner, LocalDateTime date, Pageable page);

    List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(User owner, LocalDateTime start, Pageable page);

    Booking findFirstByItemOwnerAndStartBeforeAndStatusOrderByStartDesc(
            User owner, LocalDateTime date, Status status);

    Booking findFirstByItemOwnerAndStartAfterAndStatusOrderByStartAsc(
            User owner, LocalDateTime date, Status status);

    Booking findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
            Item item, LocalDateTime date, Status status);

    Booking findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
            Item item, LocalDateTime date, Status status);
}

package ru.practicum.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.model.Status;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT book FROM Booking book, Item it " +
            "WHERE book.item.id = it.id AND it.owner = :owner " +
            "ORDER BY book.start DESC")
    List<Booking> getAllByOwner(@Param("owner") User owner);

    @Query("SELECT book FROM Booking book, Item it " +
            "WHERE book.item.id = it.id AND it.owner = :owner AND book.start > :date " +
            "ORDER BY book.start DESC")
    List<Booking> getAllByOwnerWaiting(@Param("owner") User owner, @Param("date") LocalDateTime date);

    List<Booking> findAllByBookerOrderByStartDesc(
            User booker);

    List<Booking> findAllByBookerAndStartGreaterThanOrderByStartDesc(
            User booker, LocalDateTime date);

    Booking findFirstByItemOwnerAndStartBeforeAndStatusOrderByStartDesc(
            User owner, LocalDateTime date, Status status);

    Booking findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(
            Item item, User booker, LocalDateTime date, Status status);

    Booking findFirstByItemOwnerAndStartAfterAndStatusOrderByStartAsc(
            User owner, LocalDateTime date, Status status);

    Booking findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
            Item item, LocalDateTime date, Status status);

    Booking findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
            Item item, LocalDateTime date, Status status);

    List<Booking> findAllByBookerAndStatusOrderByStartAsc(
            User booker, Status status);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
            User booker, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartAsc(
            User owner, Status status);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
            User owner, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(
            User booker, LocalDateTime date);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(
            User owner, LocalDateTime date);
}

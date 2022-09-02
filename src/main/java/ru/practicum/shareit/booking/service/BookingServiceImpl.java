package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    public List<BookingResponseDto> getAllByUserId(long userId, String state) {
        List<Booking> list;
        User booker = getUser(userId);

        switch (State.valueOf(state)) {
            case CURRENT:
                list = bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
                        booker, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                list = bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(booker, LocalDateTime.now());
                break;
            case FUTURE:
                list = bookingRepository.findAllByBookerAndStartGreaterThanOrderByStartDesc(
                        booker, LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
                break;
            case WAITING:
                list = bookingRepository.findAllByBookerAndStatusOrderByStartAsc(booker, Status.WAITING);
                break;
            case REJECTED:
                list = bookingRepository.findAllByBookerAndStatusOrderByStartAsc(booker, Status.REJECTED);
                break;
            case ALL:
                list = bookingRepository.findAllByBookerOrderByStartDesc(booker);
                break;
            default:
                list = new ArrayList<>();
        }
        log.debug("Get received all bookings for user with id: {}. Get {} bookings", userId, list.size());
        return list.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllByOwnerId(long userId, String state) {
        List<Booking> list;
        User owner = getUser(userId);

        switch (State.valueOf(state)) {
            case CURRENT:
                list = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
                        owner, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                list = bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(owner, LocalDateTime.now());
                break;
            case FUTURE:
                list = bookingRepository.getAllByOwnerWaiting(owner, LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
                break;
            case WAITING:
                list = bookingRepository.findAllByItemOwnerAndStatusOrderByStartAsc(owner, Status.WAITING);
                break;
            case REJECTED:
                list = bookingRepository.findAllByItemOwnerAndStatusOrderByStartAsc(owner, Status.REJECTED);
                break;
            case ALL:
                list = bookingRepository.getAllByOwner(owner);
                break;
            default:
                list = new ArrayList<>();
        }
        log.debug("Get received all bookings for owner with id: {}. Get {} bookings", userId, list.size());
        return list.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDto getBookingById(long userId, long bookingId) {
        Booking booking = getBooking(bookingId);
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("viewing someone else's booking is not available");
        }
        log.debug("Booking get by user id: {} and booking id: {}", userId, bookingId);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingResponseDto addBooking(long userId, BookingRequestDto bookingRequestDto) {
        User user = getUser(userId);
        Item item = getItem(bookingRequestDto.getItemId());

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Owner of the Item and the User have the same ID(" + userId + ")");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Item with Id(" + item.getId() + ") unavailable");
        }
        if (bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart())) {
            throw new ValidationException("conflict between the start and end times of the booking date " +
                    "for an item with Id(" + item.getId() + ")");
        }
        Booking booking = BookingMapper.toBooking(bookingRequestDto, item, user);
        booking.setStatus(Status.WAITING);
        BookingResponseDto bookingResponseDto = BookingMapper.toBookingDto(bookingRepository.save(booking));
        log.debug("Create new booking with id: {} for user id: {}", bookingResponseDto.getId(), userId);
        return bookingResponseDto;
    }

    @Override
    public BookingResponseDto updateBooking(long userId, long bookingId, boolean approved) {
        Booking booking = getBooking(bookingId);
        Item item = booking.getItem();

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("item does not belong to user with id: " + userId);
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("status change error");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        BookingResponseDto bookingResponseDto = BookingMapper.toBookingDto(bookingRepository.save(booking));
        log.debug("Update booking with id: {} for user id: {}", bookingResponseDto.getId(), userId);
        return bookingResponseDto;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id (" + userId + ") not found"));
    }

    private Item getItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id (" + itemId + ") not found"));
    }

    private Booking getBooking(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id (" + bookingId + ") not found"));
    }
}

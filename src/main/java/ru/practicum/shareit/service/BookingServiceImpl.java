package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.model.State;
import ru.practicum.shareit.model.Status;
import ru.practicum.shareit.dto.BookingRequestDto;
import ru.practicum.shareit.dto.BookingResponseDto;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.UserRepository;

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
    public BookingResponseDto addBooking(long userId, BookingRequestDto bookingRequestDto) {
        User user = getUserById(userId);
        Item item = getItemById(bookingRequestDto.getItemId());

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
        Booking booking = getBookingById(bookingId);
        getUserById(userId);
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

    @Override
    public BookingResponseDto getBookingByIdAndUserId(long userId, long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("viewing someone else's booking is not available");
        }
        log.debug("Booking get by user id: {} and booking id: {}", userId, bookingId);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAllByUserId(long userId, String state, int from, int size) {
        List<Booking> list;
        User booker = getUserById(userId);

        Pageable pageSortDec = PageRequest.of(from / size, size, Sort.by("start").descending());
        Pageable pageSortAsc = PageRequest.of(from / size, size, Sort.by("start").descending());

        switch (State.valueOf(state)) {
            case CURRENT:
                list = bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(
                        booker, LocalDateTime.now(), LocalDateTime.now(), pageSortDec);
                break;
            case PAST:
                list = bookingRepository
                        .findAllByBookerAndEndBeforeOrderByStartDesc(booker, LocalDateTime.now(), pageSortDec);
                break;
            case FUTURE:
                list = bookingRepository.findAllByBookerAndStartAfterOrderByStartDesc(
                        booker, LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), pageSortDec);
                break;
            case WAITING:
                list = bookingRepository.findAllByBookerAndStatusOrderByStartAsc(booker, Status.WAITING, pageSortAsc);
                break;
            case REJECTED:
                list = bookingRepository.findAllByBookerAndStatusOrderByStartAsc(booker, Status.REJECTED, pageSortAsc);
                break;
            case ALL:
                list = bookingRepository.findAllByBookerOrderByStartDesc(booker, pageSortDec);
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
    public List<BookingResponseDto> getAllByOwnerId(long userId, String state, int from, int size) {
        List<Booking> list;
        User owner = getUserById(userId);

        Pageable pageSortDec = PageRequest.of(from / size, size, Sort.by("start").descending());
        Pageable pageSortAsc = PageRequest.of(from / size, size, Sort.by("start").ascending());

        switch (State.valueOf(state)) {
            case CURRENT:
                list = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(
                        owner, LocalDateTime.now(), LocalDateTime.now(), pageSortDec);
                break;
            case PAST:
                list = bookingRepository
                        .findAllByItemOwnerAndEndBeforeOrderByStartDesc(owner, LocalDateTime.now(), pageSortDec);
                break;
            case FUTURE:
                list = bookingRepository
                        .findAllByItemOwnerAndStartAfterOrderByStartDesc(
                                owner, LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), pageSortDec);
                break;
            case WAITING:
                list = bookingRepository
                        .findAllByItemOwnerAndStatusOrderByStartAsc(owner, Status.WAITING, pageSortAsc);
                break;
            case REJECTED:
                list = bookingRepository
                        .findAllByItemOwnerAndStatusOrderByStartAsc(owner, Status.REJECTED, pageSortAsc);
                break;
            case ALL:
                list = bookingRepository.findAllByItemOwnerOrderByStartDesc(owner, pageSortDec);
                break;
            default:
                list = new ArrayList<>();
        }
        log.debug("Get received all bookings for owner with id: {}. Get {} bookings", userId, list.size());
        return list.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id (" + userId + ") not found"));
    }

    private Item getItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id (" + itemId + ") not found"));
    }

    private Booking getBookingById(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id (" + bookingId + ") not found"));
    }
}

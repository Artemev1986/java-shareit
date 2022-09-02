package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseSimpleDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemResponseSimpleDto addItem(long userId, ItemRequestDto itemDto) {
        User owner = getUser(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        itemRepository.save(item);
        log.debug("Adding new item with id: {}", item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemResponseSimpleDto getItemById(long itemId) {
        ItemResponseSimpleDto itemResponseDto = ItemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id (" + itemId + ") not found")));
        log.debug("Item get by id: {}", itemId);
        return itemResponseDto;
    }

    @Override
    public ItemResponseDto getItemByIdAndUser(long userId, long itemId) {
        Item item = getItem(itemId);
        User owner = getUser(userId);
            Booking last = bookingRepository
                    .findFirstByItemOwnerAndStartBeforeAndStatusOrderByStartDesc(
                            owner, LocalDateTime.now(), Status.APPROVED);
            Booking next = bookingRepository
                    .findFirstByItemOwnerAndStartAfterAndStatusOrderByStartAsc(
                            owner, LocalDateTime.now(), Status.APPROVED);
        log.debug("Item get by id: {} and owner id: {}", itemId, userId);
        return ItemMapper.toItemBookingDto(item, last, next, getComments(item));
    }

    private List<CommentDto> getComments(Item item) {
        return commentRepository.findAllByItemOrderByCreated(item).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseSimpleDto updateItem(long itemId, long userId, ItemRequestDto itemDto) {
        Item itemFromMemory = getItem(itemId);
        getUser(userId);
        Item item = ItemMapper.toItem(itemDto);
        if (itemFromMemory.getOwner().getId() == userId) {
            item.setId(itemId);
            if (item.getName() == null) {
                item.setName(itemFromMemory.getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(itemFromMemory.getDescription());
            }

            item.setOwner(itemFromMemory.getOwner());

            if (item.getAvailable() == null) {
                item.setAvailable(itemFromMemory.getAvailable());
            }
            itemRepository.save(item);
            log.debug("Item with id ({}) was updated", item.getId());
            return ItemMapper.toItemDto(item);
        } else {
            throw new NotFoundException("User with id (" + userId + ") doesn't have item with id (" + itemId + ")");
        }
    }

    @Override
    public List<ItemResponseDto> getUserItems(long userId) {
        User owner = getUser(userId);
        List<ItemResponseDto> itemDtoList = itemRepository.findAllByOwnerOrderByIdAsc(owner).stream()
                .map(item -> ItemMapper.toItemBookingDto(
                        item,
                        bookingRepository.findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
                                item, LocalDateTime.now(), Status.APPROVED),
                        bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
                                item, LocalDateTime.now(), Status.APPROVED), getComments(item))).collect(Collectors.toList());
        log.debug("Get user items. Current item counts: {}", itemDtoList.size());
        return itemDtoList;
    }

    @Override
    public List<ItemResponseSimpleDto> searchItems(String text) {
        List<ItemResponseSimpleDto> itemDtoList = itemRepository.findByText(text).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
        log.debug("Search items by text: {}. Found {} items", text, itemDtoList.size());
        return itemDtoList;
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        User author = getUser(userId);
        Item item = getItem(itemId);

        if (bookingRepository
                .findFirstByItemAndBookerAndStartBeforeAndStatusOrderByStartDesc(
                        item, author, LocalDateTime.now(), Status.APPROVED) == null) {
            throw new ValidationException("Item with id (" + itemId + ") was not booked by you");
        }
        Comment comment = CommentMapper.toComment(commentDto, item, author);
        log.debug("Adding new comment with id: {} for Item with id: {} by User with id: {}",
                commentDto.getId(), itemId, userId);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private Item getItem(long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with id (" + id + ") not found"));
    }

    private User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id (" + id + ") not found"));
    }
}

package ru.practicum.shareit.mapper;

import ru.practicum.shareit.dto.*;
import ru.practicum.shareit.model.Booking;
import ru.practicum.shareit.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static ItemResponseSimpleDto toItemDto(Item item) {
        ItemResponseSimpleDto itemResponseDto = new ItemResponseSimpleDto();
        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.getAvailable());
        itemResponseDto.setRequestId(item.getRequestId());
        return itemResponseDto;
    }

    public static ItemResponseDto toItemBookingDto(Item item, Booking last, Booking next,
                                                   List<CommentDto> commentsDto) {
        ItemResponseDto itemResponseBookingDto = new ItemResponseDto();
        itemResponseBookingDto.setId(item.getId());
        itemResponseBookingDto.setName(item.getName());
        itemResponseBookingDto.setDescription(item.getDescription());
        itemResponseBookingDto.setAvailable(item.getAvailable());
        BookingResponseDtoForItem lastBooking =
                last != null ? new BookingResponseDtoForItem(last.getId(), last.getBooker().getId()) : null;
        BookingResponseDtoForItem nextBooking =
                next != null ? new BookingResponseDtoForItem(next.getId(), next.getBooker().getId()) : null;
        itemResponseBookingDto.setLastBooking(lastBooking);
        itemResponseBookingDto.setNextBooking(nextBooking);
        itemResponseBookingDto.setComments(commentsDto == null ? new ArrayList<>() : commentsDto);
        return itemResponseBookingDto;
    }

    public static Item toItem(ItemRequestDto itemRequestDto) {
        Item item = new Item();
        item.setId(itemRequestDto.getId());
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());
        item.setRequestId(itemRequestDto.getRequestId());
        return item;
    }
}

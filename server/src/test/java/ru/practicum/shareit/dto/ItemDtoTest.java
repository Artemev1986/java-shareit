package ru.practicum.shareit.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JacksonTester<ItemRequestDto> jsonRequest;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JacksonTester<ItemResponseSimpleDto> jsonResponse;

    private final User user = new User();
    private final Item item = new Item();
    private ItemResponseSimpleDto itemDto;
    private final ItemRequestDto itemRequestDto = new ItemRequestDto();


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

        itemDto = ItemMapper
                .toItemDto(item);
    }

    @Test
    void itemRequestDto() throws IOException {
        JsonContent<ItemRequestDto> res = jsonRequest.write(itemRequestDto);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(res).extractingJsonPathStringValue("$.name").isEqualTo(itemRequestDto.getName());
        assertThat(res).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestDto.getDescription());
        assertThat(res).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemRequestDto.getAvailable());
    }

    @Test
    void itemResponseDto() throws IOException {
        JsonContent<ItemResponseSimpleDto> res = jsonResponse.write(itemDto);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(res).extractingJsonPathStringValue("$.name").isEqualTo(itemRequestDto.getName());
        assertThat(res).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestDto.getDescription());
        assertThat(res).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemRequestDto.getAvailable());
    }

}
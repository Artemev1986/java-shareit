package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.dto.ItemRequestDto;
import ru.practicum.shareit.dto.ItemResponseSimpleDto;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.service.ItemService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemIntegrationTest {
    private final UserRepository userRepository;
    private final ItemService itemService;
    private final User owner = new User();
    private final Item item = new Item();
    private final ItemRequestDto itemRequestDto = new ItemRequestDto();

    @Test
    void testItem() {
        owner.setName("Mikhail");
        owner.setEmail("test@gmail.com");
        userRepository.save(owner);

        item.setName("Item1");
        item.setDescription("Item1 description");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequestId(null);

        itemRequestDto.setName("Item1");
        itemRequestDto.setDescription("Item1 description");
        itemRequestDto.setAvailable(true);
        itemRequestDto.setRequestId(null);

        itemService.addItem(owner.getId(), itemRequestDto);

        List<ItemResponseSimpleDto> itemList = itemService.searchItems("Item1", 0, 10);

        assertThat(itemList.get(0).getName()).isEqualTo("Item1");
    }
}

package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.Request;
import ru.practicum.shareit.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
class ItemRepositoryTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private List<Item> itemList;
    private final User user = new User();
    private final Item item1 = new Item();
    private final Item item2 = new Item();
    private final Item item3 = new Item();
    private final Request request = new Request();
    private final Pageable page = PageRequest.of(0, 10);

    @BeforeEach
    void beforeEach() {
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");

        request.setDescription("request1");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());

        entityManager.persist(user);
        entityManager.persist(request);

        item1.setName("Item1");
        item1.setDescription("Item1 description1");
        item1.setAvailable(true);
        item1.setOwner(user);
        item1.setRequestId(request.getId());

        item2.setName("Item2");
        item2.setDescription("Item2 description2");
        item2.setAvailable(true);
        item2.setOwner(user);
        item2.setRequestId(null);

        item3.setName("Item3");
        item3.setDescription("Item3 description3");
        item3.setAvailable(true);
        item3.setOwner(user);
        item3.setRequestId(null);

        entityManager.persist(item1);
        entityManager.persist(item2);
        entityManager.persist(item3);
    }

    @Test
    void findAllByOwner() {
        itemList = itemRepository.findAllByOwnerOrderByIdAsc(user, page);

        assertThat(itemList).isEqualTo(List.of(item1, item2, item3));
    }

    @Test
    void findByText() {
        itemList = itemRepository.findByText("item2", page);

        assertThat(itemList).isEqualTo(List.of(item2));
    }

    @Test
    void findAllByRequestId() {
        itemList = itemRepository.findAllByRequestId(1L);

        assertThat(itemList).isEqualTo(List.of(item1));
    }
}
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
class RequestRepositoryTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RequestRepository requestRepository;

    private List<Request> requestList;
    private final User user = new User();
    private final Item item = new Item();
    private final Request request = new Request();
    private final Pageable page = PageRequest.of(0, 10);

    @BeforeEach
    void beforeEach() {
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");

        request.setDescription("request1");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());

        item.setName("Item1");
        item.setDescription("Item1 description1");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(null);

        entityManager.persist(user);
        entityManager.persist(request);
        entityManager.persist(item);
    }

    @Test
    void findAllByRequestorOrderByCreatedDesc() {
        requestList = requestRepository.findAllByRequestorOrderByCreatedDesc(user);
        assertThat(requestList).isEqualTo(List.of(request));
    }

    @Test
    void findAllByRequestorNotOrderByCreatedDesc() {
        requestList = requestRepository.findAllByRequestorNotOrderByCreatedDesc(user, page);
        assertThat(requestList).isEqualTo(List.of());
    }
}
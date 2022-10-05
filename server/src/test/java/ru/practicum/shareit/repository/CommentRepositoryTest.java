package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.model.Comment;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
class CommentRepositoryTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    private final User user = new User();
    private final Item item = new Item();
    private final Comment comment = new Comment();

    @BeforeEach
    void beforeEach() {
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");

        item.setName("Item1");
        item.setDescription("Item1 description1");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequestId(null);

        comment.setAuthor(user);
        comment.setItem(item);
        comment.setText("comment");
        comment.setCreated(LocalDateTime.now());

        entityManager.persist(user);
        entityManager.persist(item);
        entityManager.persist(comment);
    }

    @Test
    void findAllByItemOrderByCreated() {
        List<Comment> commentList = commentRepository.findAllByItemOrderByCreated(item);

        assertThat(commentList).isEqualTo(List.of(comment));
    }
}
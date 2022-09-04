package ru.practicum.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.Comment;
import ru.practicum.shareit.model.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemOrderByCreated(Item item);
}

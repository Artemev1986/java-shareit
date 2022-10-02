package ru.practicum.shareit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.Request;
import ru.practicum.shareit.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequestorOrderByCreatedDesc(User requestor);

    List<Request> findAllByRequestorNotOrderByCreatedDesc(User requestor, Pageable page);
}

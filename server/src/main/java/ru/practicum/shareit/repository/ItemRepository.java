package ru.practicum.shareit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.model.Item;
import ru.practicum.shareit.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerOrderByIdAsc(User owner, Pageable page);

    @Query("SELECT it FROM Item it WHERE it.available = TRUE AND " +
            "(UPPER(it.name) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(it.description) LIKE UPPER(CONCAT('%',:text,'%'))) " +
            "ORDER BY it.id ASC")
    List<Item> findByText(@Param("text") String text, Pageable page);

    List<Item> findAllByRequestId(long requestId);
}
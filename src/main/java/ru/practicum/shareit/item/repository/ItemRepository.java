package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerOrderByIdAsc(User owner);

    @Query("SELECT i FROM Item i WHERE i.available = TRUE AND " +
            "(UPPER(i.name) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%',:text,'%'))) " +
            "ORDER BY i.id ASC")
    List<Item> findByText(@Param("text") String text);
}
package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findById(long itemId);

    List<Item> findByUserId(long userId);

    @Query("select it from Item as it " +
            "where (lower(it.name) like concat('%', lower(:text), '%')  " +
            "or lower(it.description) like concat('%', lower(:text), '%')) " +
            "and it.available is true")
    List<Item> findItemsByText(String text);

    List<Item> findByRequestId(long requestId);
}

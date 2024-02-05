package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Method searches for all items owned by user in repository.
     *
     * @param userId ID of user.
     * @return List of items owned by user.
     */
    List<Item> findByOwnerIdOrderById(Long userId);

    /**
     * Method gets all items which name or description contain text.
     *
     * @param text Search request.
     * @return List of items which name or description contain text.
     */
    @Query(value = "" +
            "SELECT i " +
            "FROM Item AS i " +
            "WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND i.available = true")
    List<Item> search(String text);
}

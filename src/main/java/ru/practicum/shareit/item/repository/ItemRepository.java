package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Method searches for all items owned by user in repository.
     *
     * @param userId ID of user.
     * @param page Pagination parameter.
     *
     * @return List of items owned by user.
     */
    Page<Item> findByOwnerId(Long userId, Pageable page);

    /**
     * Method gets all items which name or description contain text.
     *
     * @param text Search request.
     * @param page Pagination parameter.
     *
     * @return List of items which name or description contain text.
     */
    @Query(value = "" +
            "SELECT i " +
            "FROM Item AS i " +
            "WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND i.available = true")
    Page<Item> search(String text, Pageable page);

    /**
     * Method gets all items by request id.
     *
     * @param requestId ID of item request.
     *
     * @return Set of items with specific request ID.
     */
    Set<Item> findAllByRequestId(Long requestId);
}

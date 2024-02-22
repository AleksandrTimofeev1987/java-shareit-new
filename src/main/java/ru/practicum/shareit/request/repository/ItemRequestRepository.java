package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    /**
     * Method searches for all item requests created by user in repository.
     *
     * @param userId ID of user.
     *
     * @return List of item requests owned by user.
     */
    List<ItemRequest> findAllByRequesterIdOrderByCreatedDesc(Long userId);

    /**
     * Method return all item requests.
     *
     * @param userId ID of user requesting item requests.
     * @param page Pagination parameter.
     *
     * @return List of item requests.
     */
    Page<ItemRequest> findItemRequestsByRequester_IdIsNot(Long userId, Pageable page);

}


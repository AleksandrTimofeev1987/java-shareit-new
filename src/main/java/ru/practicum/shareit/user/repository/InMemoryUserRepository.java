package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class InMemoryUserRepository implements UserRepository {

    private Long globalId = 1L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User findById(Long userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        user.setId(globalId);
        users.put(globalId, user);

        globalId++;

        return users.get(user.getId());
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    @Override
    public void checkEmailDuplication(String email) {
        if (email != null && users
                .values()
                .stream()
                .map(User::getEmail)
                .anyMatch(e -> e.equals(email))) {
            final String message = String.format("Storage already contain user with email - %s.", email);
            log.warn(message);
            throw new ConflictException(message);
        }
    }

    @Override
    public void checkUserExists(Long userId) {
        if (findById(userId) == null) {
            final String message = String.format("User with ID - %d is not found.", userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }
}

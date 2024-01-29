package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    /**
     * Method searches for certain user in repository.
     *
     * @param userId ID of user to be found.
     *
     * @return Found user or null.
     */
    User findById(Long userId);

    /**
     * Method returnes all users in repository.
     *
     * @return List of all users in repository.
     */
    List<User> findAll();

    /**
     * Method saves new user in repository.
     *
     * @param user User to be saved.
     *
     * @return Saved user with assigned ID.
     */
    User save(User user);

    /**
     * Method deleted certain user in repository.
     *
     * @param userId ID of user to be deleted.
     */
    void delete(Long userId);

    /**
     * Method checks if repository already contain user with certain email and throw ConflictException if found.
     *
     * @param email Email to be checked.
     */
    void checkEmailDuplication(String email);

    /**
     * Method checks if repository contain user with certain ID.
     *
     * @param userId User ID to be checked.
     */
    void checkUserExists(Long userId);
}

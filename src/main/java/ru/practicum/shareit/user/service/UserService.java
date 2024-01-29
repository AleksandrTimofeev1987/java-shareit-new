package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    /**
     * Method gets user by ID.
     *
     * @param userId ID of user to be returned.
     *
     * @return User with specific ID.
     */
    UserResponseDto getUser(Long userId);

    /**
     * Method gets all users.
     *
     * @return List of all users.
     */
    List<UserResponseDto> getAllUsers();

    /**
     * Method creates new user.
     *
     * @param user User to be added.
     *
     * @return Added user response DTO with assigned ID.
     */
    UserResponseDto createUser(User user);

    /**
     * Method updates user.
     *
     * @param userId ID of user to be updated.
     * @param userDto User to be updated.
     *
     * @return Updated user response DTO.
     */
    UserResponseDto updateUser(Long userId, UserUpdateDto userDto);

    /**
     * Method deletes user by ID.
     *
     * @param userId ID of user to be deleted.
     */
    void deleteUser(Long userId);
}

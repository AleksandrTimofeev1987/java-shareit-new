package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String EMAIL_DUPLICATE_MESSAGE = "User email should be unique.";

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        log.debug("Request to get user with ID - {} is received.", userId);

        User userFromRepository = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId)));

        log.debug("User with ID - {} is received from repository.", userFromRepository.getId());
        return userMapper.toUserResponseDto(userFromRepository);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDto createUser(User user) {
        log.debug("Request to add new user with name - {} is received.", user.getName());

        User savedUser;

        try {
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.warn(EMAIL_DUPLICATE_MESSAGE);
            throw new ConflictException(EMAIL_DUPLICATE_MESSAGE);
        }

        log.debug("User with ID - {} is added to repository.", savedUser.getId());

        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateDto userDto) {
        log.debug("Request to update user with ID - {} is received.", userId);

        User userForUpdate = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id: %d is not found", userId)));

        userMapper.toUserFromUserUpdateDto(userDto, userForUpdate);

        User updatedUser;

        try {
            updatedUser = userRepository.save(userForUpdate);
        } catch (DataIntegrityViolationException e) {
            log.warn(EMAIL_DUPLICATE_MESSAGE);
            throw new ConflictException(EMAIL_DUPLICATE_MESSAGE);
        }

        log.debug("User with ID - {} is updated in repository.", updatedUser.getId());

        return userMapper.toUserResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.debug("Request to delete user with ID - {} is received.", userId);

        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            final String message = String.format("User with id: %d is not found", userId);
            log.warn(message);
            throw new NotFoundException(message);
        }

        log.debug("User with ID - {} is deleted from repository.", userId);
    }
}

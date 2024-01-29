package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    @Override
    public UserResponseDto getUser(Long userId) {
        log.debug("Request to get user with ID - {} is received.", userId);
        userRepository.checkUserExists(userId);

        User userFromRepository = userRepository.findById(userId);

        log.debug("User with ID - {} is received from repository.", userFromRepository.getId());
        return UserMapper.toUserResponseDto(userFromRepository);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(UserMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto createUser(User user) {
        log.debug("Request to add new user with name - {} is received.", user.getName());

        userRepository.checkEmailDuplication(user.getEmail());

        User savedUser = userRepository.save(user);

        log.debug("User with ID - {} is added to repository.", savedUser.getId());

        return UserMapper.toUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserUpdateDto userDto) {
        log.debug("Request to update user with ID - {} is received.", userId);

        userRepository.checkUserExists(userId);
        userRepository.checkEmailDuplication(userDto.getEmail());

        User userForUpdate = userRepository.findById(userId);

        UserMapper.toUserFromUserUpdateDto(userDto, userForUpdate);

        User updatedUser = userRepository.findById(userId);

        log.debug("User with ID - {} is updated in repository.", updatedUser.getId());

        return UserMapper.toUserResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        log.debug("Request to delete user with ID - {} is received.", userId);
        userRepository.checkUserExists(userId);
        userRepository.delete(userId);
        log.debug("User with ID - {} is deleted from repository.", userId);
    }
}

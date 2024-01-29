package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    public static UserResponseDto toUserResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserCreateDto userDto) {
        if (userDto == null) {
            return null;
        }

        return new User(
                null,
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static void toUserFromUserUpdateDto(UserUpdateDto userDto, User user) {

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
    }
}

package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto getUserById(long userId);

    UserDto updateUser(long userId, UserDto userDto);

    void deleteUserById(long userId);

    List<UserDto> getAllUsers();
}

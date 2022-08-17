package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (userStorage.isExistEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email (" + user.getEmail() + ") already exist");
        }
        userStorage.addUser(user);
        log.debug("Adding new user with id: {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUserById(long userId) {
        UserDto userDto = UserMapper.toUserDto(userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id (" + userId + ") not found")));
        log.debug("User get by id: {}", userId);
        return userDto;
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User useFromMemory = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id (" + userId + ") not found"));
        User user = UserMapper.toUser(userDto);
        user.setId(userId);
        if (userStorage.isExistEmail(user.getEmail())) {
            throw new RuntimeException("User with email (" + user.getEmail() + ") already exist");
        }
        if (user.getEmail() == null) {
            user.setEmail(useFromMemory.getEmail());
        }
        if (user.getName() == null) {
                user.setName(useFromMemory.getName());
        }
        userStorage.updateUser(user);
        log.debug("User with id ({}) was updated", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(long userId) {
        userStorage.deleteUserById(userId);
        log.debug("User with id ({}) was deleted", userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = userStorage.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        log.debug("Get all users. Current user counts: {}", usersDto.size());
        return usersDto;
    }
}

package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        userRepository.save(user);
        log.debug("Adding new user with id: {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUserById(long userId) {
        UserDto userDto = UserMapper.toUserDto(getUser(userId));
        log.debug("User get by id: {}", userId);
        return userDto;
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User updatedUser = getUser(userId);
        if (userDto.getName() != null) {
            updatedUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            updatedUser.setEmail(userDto.getEmail());
        }
        userRepository.save(updatedUser);
        log.debug("User with id ({}) was updated", updatedUser.getId());
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUserById(long userId) {
        getUser(userId);
        userRepository.deleteById(userId);
        log.debug("User with id ({}) was deleted", userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = userRepository.findAll()
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        log.debug("Get all users. Current user counts: {}", usersDto.size());
        return usersDto;
    }

    private User getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id (" + id + ") not found"));
        log.debug("User get by id: {}", id);
        return user;
    }
}

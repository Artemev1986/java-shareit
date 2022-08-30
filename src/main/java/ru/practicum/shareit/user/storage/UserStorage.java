package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    void addUser(User user);

    Optional<User> getUserById(long id);

    void updateUser(User user);

    List<User> getAllUsers();

    void deleteUserById(long id);
}

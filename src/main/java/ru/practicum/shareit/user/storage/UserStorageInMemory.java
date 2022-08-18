package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserStorageInMemory implements UserStorage {
    private long id;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        checkEmail(user);
        id++;
        user.setId(id);
        users.put(id, user);
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void updateUser(User user) {
        checkEmail(user);
        if (user.getEmail() == null) {
            user.setEmail(users.get(user.getId()).getEmail());
        }
        if (user.getName() == null) {
            user.setName(users.get(user.getId()).getName());
        }
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public void deleteUserById(long id) {
        users.remove(id);
    }

    private void checkEmail(User user) {
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new IllegalArgumentException("User with email (" + user.getEmail() + ") already exist");
        }
    }
}

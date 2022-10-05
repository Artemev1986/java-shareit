package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Validated(Create.class) UserDto userDto) {
        log.info("Creating user {}", userDto);
        return userClient.addUser(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        log.info("Getting user by id {}", userId);
        return userClient.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Getting all users");
        return userClient.getAllUsers();
    }

    @PatchMapping("/{userID}")
    public ResponseEntity<Object> updateUser(@PathVariable long userID, @RequestBody @Valid UserDto userDto) {
        log.info("Updating user {}", userDto);
        return userClient.updateUser(userID, userDto);
    }

    @DeleteMapping("/{userID}")
    public void deleteUserById(@PathVariable long userID) {
        log.info("Deleting user by id {}", userID);
        userClient.deleteUserById(userID);
    }
}

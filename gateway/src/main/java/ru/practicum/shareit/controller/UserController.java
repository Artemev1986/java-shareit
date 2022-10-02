package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.dto.UserDto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Validated(Create.class) UserDto userDto) {
        return userClient.addUser(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        return userClient.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        ResponseEntity<Object> userList = userClient.getAllUsers();
        return userList;
    }

    @PatchMapping("/{userID}")
    public ResponseEntity<Object> updateUser(@PathVariable long userID,
                         @RequestBody @Valid UserDto userDto) {
        return userClient.updateUser(userID, userDto);
    }

    @DeleteMapping("/{userID}")
    public void deleteUserById(@PathVariable long userID) {
        userClient.deleteUserById(userID);
    }
}

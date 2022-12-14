package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtoList = userService.getAllUsers();
        if (userDtoList.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @PatchMapping("/{userID}")
    public UserDto updateUser(@PathVariable long userID,
                         @RequestBody UserDto userDto) {
        return userService.updateUser(userID, userDto);
    }

    @DeleteMapping("/{userID}")
    public ResponseEntity<Object> deleteUserById(@PathVariable long userID) {
        userService.deleteUserById(userID);
        return ResponseEntity.ok().build();
    }
}

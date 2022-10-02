package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {

    private final UserService userService;

    @MockBean
    private final UserRepository userRepository;

    private final User user = new User();

    @BeforeEach
    void beforeEach() {
        user.setId(1L);
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");
    }

    @Test
    void addUser() {
        Mockito
                .when(userRepository.save(any()))
                .thenReturn(user);

        UserDto userDto = userService.addUser(UserMapper.toUserDto(user));

        assertThat(userDto).isEqualTo(UserMapper.toUserDto(user));
    }

    @Test
    void getUserById() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(user.getId());

        assertThat(userDto).isEqualTo(UserMapper.toUserDto(user));
    }

    @Test
    void updateUser() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(userRepository.save(any()))
                .thenReturn(user);

        UserDto userDto = userService.updateUser(user.getId(), UserMapper.toUserDto(user));

        assertThat(userDto).isEqualTo(UserMapper.toUserDto(user));
    }

    @Test
    void getAllUsers() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserDto> usersDto = userService.getAllUsers();

        assertThat(usersDto).isEqualTo(Stream.of(user).map(UserMapper::toUserDto).collect(Collectors.toList()));
    }
}
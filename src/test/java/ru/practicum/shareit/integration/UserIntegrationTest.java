package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.dto.UserDto;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserIntegrationTest {
    private final UserRepository userRepository;

    @Test
    void testUser() {
    User user = new User();
        user.setName("Mikhail");
        user.setEmail("test@gmail.com");

        userRepository.save(user);
    Optional<User> userOptional = userRepository.findById(1L);
    assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
    assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "Mikhail")
                                .hasFieldOrPropertyWithValue("email", "test@gmail.com")
            );

        User user2 = new User();
        user2.setName("Oleg");
        user2.setEmail("oleg@gmail.com");

        userRepository.save(user2);

        List<UserDto> usersDto = userRepository.findAll()
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        assertThat(usersDto)
                .isEqualTo(Stream.of(user, user2).map(UserMapper::toUserDto).collect(Collectors.toList()));
    }
}

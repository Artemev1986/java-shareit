package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.dto.RequestQueryDto;
import ru.practicum.shareit.dto.RequestResponseDto;
import ru.practicum.shareit.model.User;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.service.RequestService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestIntegrationTest {
    private final RequestService requestService;
    private final UserRepository userRepository;
    private final User user = new User();

    private final RequestQueryDto requestQueryDto = new RequestQueryDto();

    @Test
    void testRequest() {
        user.setName("Mikhail");
        user.setEmail("Mikhail@gmail.com");

        userRepository.save(user);

        requestQueryDto.setDescription("description");

        requestService.addRequest(requestQueryDto, user.getId());

        List<RequestResponseDto> requestResponseDtoList = requestService.getAllByUserId(user.getId());

        assertThat(requestResponseDtoList.size())
                .isEqualTo(1);
    }
}

package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestDtoTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JacksonTester<RequestResponseDto> json;

    private final RequestResponseDto requestDto = new RequestResponseDto();

    @Test
    void requestDto() throws IOException {
        requestDto.setId(1L);
        requestDto.setDescription("request");
        requestDto.setCreated(LocalDateTime.of(2022, 9, 20, 1, 1, 1));
        requestDto.setItems(List.of());

        JsonContent<RequestResponseDto> res = json.write(requestDto);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(res).extractingJsonPathStringValue("$.description")
                .isEqualTo(requestDto.getDescription());
        assertThat(res).extractingJsonPathStringValue("$.created")
                .isEqualTo(requestDto.getCreated().toString());
        assertThat(res).extractingJsonPathArrayValue("$.items")
                .isEqualTo(requestDto.getItems());
    }

}
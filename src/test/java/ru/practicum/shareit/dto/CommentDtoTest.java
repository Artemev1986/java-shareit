package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JacksonTester<CommentDto> json;

    private final CommentDto commentDto = new CommentDto();

    @Test
    void commentDto() throws IOException {
        commentDto.setId(1L);
        commentDto.setText("text");
        commentDto.setAuthorName("Mikhail");
        commentDto.setCreated(LocalDateTime.of(2022, 9, 20, 1, 1, 1));

        JsonContent<CommentDto> res = json.write(commentDto);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(res).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDto.getAuthorName());
        assertThat(res).extractingJsonPathStringValue("$.created").isEqualTo(commentDto.getCreated().toString());
        assertThat(res).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
    }

}
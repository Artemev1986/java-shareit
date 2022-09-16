package ru.practicum.shareit.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.model.Status;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JacksonTester<BookingRequestDto> jsonReq;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JacksonTester<BookingResponseDto> jsonResp;

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto();
    private final BookingResponseDto bookingResponseDto = new BookingResponseDto();

    LocalDateTime start = LocalDateTime.of(2022, 9, 20, 1, 1, 1);
    LocalDateTime end = LocalDateTime.of(2022, 9, 21, 1, 1, 1);

    @BeforeEach
    void BeforeEach() {
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(start);
        bookingRequestDto.setEnd(end);

        bookingResponseDto.setId(1L);
        bookingResponseDto.setStart(start);
        bookingResponseDto.setEnd(end);
        bookingResponseDto.setStatus(Status.WAITING);
    }

    @Test
    void bookingRequestDtoTest() throws IOException {
        JsonContent<BookingRequestDto> res = jsonReq.write(bookingRequestDto);

        assertThat(res).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(res).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingRequestDto.getStart().toString());
        assertThat(res).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingRequestDto.getEnd().toString());
    }

    @Test
    void bookingResponseDtoTest() throws IOException {
        JsonContent<BookingResponseDto> res = jsonResp.write(bookingResponseDto);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(res).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingResponseDto.getStart().toString());
        assertThat(res).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingResponseDto.getEnd().toString());
        assertThat(res).extractingJsonPathStringValue("$.status")
                .isEqualTo(bookingResponseDto.getStatus().toString());
    }

}
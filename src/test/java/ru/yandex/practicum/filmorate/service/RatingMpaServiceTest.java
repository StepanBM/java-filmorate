package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.RatingMpaRepository;
import ru.yandex.practicum.filmorate.dal.mappers.RatingMpaRowMapper;
import ru.yandex.practicum.filmorate.dto.RatingMpaDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({RatingMpaService.class,
        RatingMpaRepository.class,
        RatingMpaRowMapper.class
})
public class RatingMpaServiceTest {

    private final RatingMpaService ratingMpaService;

    @Autowired
    public RatingMpaServiceTest(RatingMpaService ratingMpaService) {
        this.ratingMpaService = ratingMpaService;
    }

    @Test
    void getRatingMpaByIdTest() {

        RatingMpaDto mpaDto = ratingMpaService.getRatingMpaById(3);

        assertThat(mpaDto.getName()).isEqualTo("PG-13");

    }

    @Test
    void getRatingMpaTest() {

        List<RatingMpaDto> mpa = ratingMpaService.getRatingMpa();

        assertThat(mpa).hasSize(5);
        assertThat(mpa.get(1).getName()).isEqualTo("PG");
        assertThat(mpa.get(4).getName()).isEqualTo("NC-17");

    }
}

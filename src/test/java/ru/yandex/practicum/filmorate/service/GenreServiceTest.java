package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({GenreService.class,
        GenreRepository.class,
        GenreRowMapper.class
})
public class GenreServiceTest {

    private final GenreService genreService;

    @Autowired
    public GenreServiceTest(GenreService genreService) {
        this.genreService = genreService;
    }

    @Test
    void getGenreByIdTest() {
        GenreDto genreDto = genreService.getGenreById(5);

        assertThat(genreDto.getName()).isEqualTo("Документальный");
    }

    @Test
    void getGenreTest() {

        List<GenreDto> genre = genreService.getGenre();

        assertThat(genre).hasSize(6);
        assertThat(genre.get(0).getName()).isEqualTo("Комедия");
        assertThat(genre.get(3).getName()).isEqualTo("Триллер");
    }
}

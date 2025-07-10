package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.RatingMpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.RatingMpaRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({
        FilmRepository.class,
        UserRepository.class,
        UserService.class,
        GenreService.class,
        GenreRepository.class,
        RatingMpaService.class,
        FilmService.class,
        FilmRowMapper.class,
        RatingMpaRepository.class,
        GenreRowMapper.class,
        RatingMpaRowMapper.class,
        UserRowMapper.class
})
public class FilmServiceTest {

    private final FilmService filmService;
    private final FilmRepository filmRepository;
    private final UserService userService;
    // Добавляем JdbcTemplate для очистки БД
    private final JdbcTemplate jdbcTemplate;

    NewFilmRequest request1;
    NewFilmRequest request2;

    NewUserRequest newUser;

    @Autowired
    public FilmServiceTest(FilmService filmService, FilmRepository filmRepository, UserService userService, JdbcTemplate jdbcTemplate) {
        this.filmService = filmService;
        this.filmRepository = filmRepository;
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE films ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");

        newUser = new NewUserRequest();
        newUser.setName("Тестовый пользователь");
        newUser.setLogin("Step007");
        newUser.setEmail("mail9999@yandex.ru");
        newUser.setBirthday(LocalDate.of(1997, 11, 15));

        request1 = new NewFilmRequest();
        request1.setName("Тестовый фильм №1");
        request1.setDescription("Описание фильма");
        request1.setReleaseDate(LocalDate.of(2021, 1, 12));
        request1.setDuration(120);
        NewRatingMpaRequest mpa = new NewRatingMpaRequest();
        mpa.setId(2);
        request1.setMpa(mpa);
        NewGenreRequest genre1 = new NewGenreRequest();
        NewGenreRequest genre2 = new NewGenreRequest();
        List<NewGenreRequest> listGenre = new LinkedList<>();
        genre1.setId(3);
        genre2.setId(5);
        listGenre.add(genre1);
        listGenre.add(genre2);
        request1.setGenres(listGenre);


        request2 = new NewFilmRequest();
        request2.setName("Тестовый фильм №2");
        request2.setDescription("Описание фильма");
        request2.setReleaseDate(LocalDate.of(2025, 5, 21));
        request2.setDuration(180);
        NewRatingMpaRequest mpa2 = new NewRatingMpaRequest();
        mpa2.setId(5);
        request2.setMpa(mpa2);

    }

    @Test
    void createFilmTest() {

        FilmDto createdFilm = filmService.createFilm(request1);
        assertThat(createdFilm).isNotNull();
        assertThat(createdFilm.getName()).isEqualTo("Тестовый фильм №1");
        assertThat(createdFilm.getDescription()).isEqualTo("Описание фильма");
        assertThat(createdFilm.getReleaseDate()).isEqualTo("2021-01-12");
        assertThat(createdFilm.getDuration()).isEqualTo(120L);
        assertThat(createdFilm.getMpa().getName()).isEqualTo("PG");
        assertThat(createdFilm.getGenres()).hasSize(2);
        assertThat(createdFilm.getGenres()).extracting("name").contains("Мультфильм");
        assertThat(createdFilm.getGenres()).extracting("name").contains("Документальный");
    }

    @Test
    void getFilmByIdTest() {

        filmService.createFilm(request1);
        Optional<Film> filmOptional = filmRepository.findById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getId()).isEqualTo(1);
                    assertThat(film.getName()).isEqualTo("Тестовый фильм №1");
                });
    }

    @Test
    void getFilmsTest() {
        filmService.createFilm(request1);
        filmService.createFilm(request2);
        List<Film> films = filmRepository.findAll();

        assertThat(films).hasSize(2);
        assertThat(films.get(0).getName()).isEqualTo("Тестовый фильм №1");
        assertThat(films.get(1).getName()).isEqualTo("Тестовый фильм №2");
    }

    @Test
    void updateFilmTest() {

        filmService.createFilm(request1);

        Film filmUpdate = new Film();
        filmUpdate.setId(1L);
        filmUpdate.setName("Обновленное название фильма");
        filmUpdate.setDescription("Обновленное описание фильма");
        filmUpdate.setReleaseDate(LocalDate.of(1999, 9, 23));
        filmUpdate.setDuration(79);

        filmRepository.update(filmUpdate);

        Optional<Film> updatedFilmOptional = filmRepository.findById(1);
        assertThat(updatedFilmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getName()).isEqualTo("Обновленное название фильма");
                    assertThat(film.getDescription()).isEqualTo("Обновленное описание фильма");
                    assertThat(film.getReleaseDate()).isEqualTo("1999-09-23");
                    assertThat(film.getDuration()).isEqualTo(79);
                });
    }

    @Test
    void addLikeTset() {
        filmService.createFilm(request1);
        userService.createUser(newUser);

        filmService.addLike(1, 1);

        Optional<Film> filmOptional = filmRepository.findById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getId()).isEqualTo(1);
                    assertThat(film.getName()).isEqualTo("Тестовый фильм №1");
                    assertThat(film.getFilmLikesCount()).isEqualTo(1);
                });

    }

    @Test
    void deleteLikeTest() {
        filmService.createFilm(request1);
        userService.createUser(newUser);

        filmService.addLike(1, 1);

        Optional<Film> filmOptional = filmRepository.findById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                    System.out.println(film.getFilmLikesCount());
                    assertThat(film.getName()).isEqualTo("Тестовый фильм №1");
                    assertThat(film.getFilmLikesCount()).isEqualTo(1L);
                });
        filmService.deleteLike(1, 1);

        Optional<Film> filmUpdateOptional = filmRepository.findById(1);

        assertThat(filmUpdateOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getName()).isEqualTo("Тестовый фильм №1");
                    assertThat(film.getFilmLikesCount()).isEqualTo(0L);
                });
    }

    @Test
    void getPopularTest() {

        filmService.createFilm(request1);
        filmService.createFilm(request2);
        userService.createUser(newUser);
        filmService.addLike(2, 1);

        List<Film> popularFilms = filmRepository.getPopularFilms(2);
        assertThat(popularFilms).hasSize(2);
    }
}
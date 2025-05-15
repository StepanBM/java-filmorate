package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    // Вспомогательный метод для создания фильма
    private Film createValidFilm() {
        Film film = new Film();
        film.setName("Название фильма");
        film.setDescription("Оооооочень длинное описание фильма");
        film.setReleaseDate(LocalDate.of(2021, 5, 12));
        film.setDuration(70);
        return film;
    }

    // Вспомогательный метод для обновления фильма
    private Film updateValidFilm() {
        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("Обновленное название фильма");
        updateFilm.setDescription("Обновленное описание фильма");
        updateFilm.setReleaseDate(LocalDate.of(2025, 7, 21));
        updateFilm.setDuration(210);
        return updateFilm;
    }

    @Test
    public void testCreateFilm() {

        Film film = createValidFilm();
        Film createdFilm = filmController.create(film);

        assertNotNull(createdFilm);

        Collection<Film> collectionFilm = filmController.findAll();

        assertEquals(1, collectionFilm.size(), "Некорректное количество фильмов");
        assertEquals("Название фильма", collectionFilm.iterator().next().getName(), "Некорректное название фильма");
        assertEquals("Оооооочень длинное описание фильма", collectionFilm.iterator().next().getDescription(), "Некорректное описание фильма");
        assertEquals(createdFilm.getReleaseDate(), collectionFilm.iterator().next().getReleaseDate(), "Некорректное дата фильма");
        assertEquals(70, collectionFilm.iterator().next().getDuration(), "Некорректное продолжительность фильма");
    }

    @Test
    public void testUpdateFilm() {

        Film film = createValidFilm();
        filmController.create(film);

        Film newFilm = updateValidFilm();
        Film updatedFilm = filmController.update(newFilm);

        assertEquals(1, updatedFilm.getId());
        assertEquals("Обновленное название фильма", updatedFilm.getName());
        assertEquals("Обновленное описание фильма", updatedFilm.getDescription());
        assertEquals(LocalDate.of(2025, 7, 21), updatedFilm.getReleaseDate());
        assertEquals(210, updatedFilm.getDuration());

        Collection<Film> collectionFilm = filmController.findAll();

        assertEquals(1, collectionFilm.size(), "Некорректное количество фильмов");
        assertEquals("Обновленное название фильма", collectionFilm.iterator().next().getName(), "Некорректное название фильма");
        assertEquals("Обновленное описание фильма", collectionFilm.iterator().next().getDescription(), "Некорректное описание фильма");
        assertEquals(updatedFilm.getReleaseDate(), collectionFilm.iterator().next().getReleaseDate(), "Некорректное дата фильма");
        assertEquals(210, collectionFilm.iterator().next().getDuration(), "Некорректное продолжительность фильма");
    }

    @Test
    public void testValidationFilmErrorReleaseDate() {

        Film film = createValidFilm();
        film.setReleaseDate(LocalDate.of(1894, 5, 12));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.create(film);
                });

        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    public void testValidationFilmErrorUnknownId() {

        Film film = createValidFilm();
        filmController.create(film);

        Film newFilm = updateValidFilm();
        newFilm.setId(5);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.update(newFilm);
                });

        assertEquals("Фильм с id =" + newFilm.getId() + " не найден", exception.getMessage());
    }

}

package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {

    private FilmController filmController;
    private InMemoryFilmStorage inMemoryFilmStorage;
    private UserController userController;
    private InMemoryUserStorage inMemoryUserStorage;
    private UserService userService;
    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        inMemoryUserStorage = new InMemoryUserStorage();
        userService = new UserService(inMemoryUserStorage);
        userController = new UserController(inMemoryUserStorage, userService);
        inMemoryFilmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);
        filmController = new FilmController(inMemoryFilmStorage, filmService);
    }

    private Film createValidFilm() {
        Film film = new Film();
        film.setName("Название фильма");
        film.setDescription("Оооооочень длинное описание фильма");
        film.setReleaseDate(LocalDate.of(2021, 5, 12));
        film.setDuration(70);
        return film;
    }

    private User createValidUser1() {
        User user = new User();
        user.setEmail("BigBos@mail.ru");
        user.setLogin("Arak101010");
        user.setName("Alexander");
        user.setBirthday(LocalDate.of(1997, 7, 3));
        return user;
    }

    private User createValidUser2() {
        User user2 = new User();
        user2.setEmail("Jumal@mail.ru");
        user2.setLogin("Jum112");
        user2.setName("Sergey");
        user2.setBirthday(LocalDate.of(1999, 7, 3));
        return user2;
    }

    @Test
    public void testCreateFilm() {

        User user = createValidUser1();
        User user2 = createValidUser2();

        Film film = createValidFilm();

        User createdUser = userController.create(user);
        User createdUser2 = userController.create(user2);

        Film createdFilm = filmController.create(film);

        assertNotNull(createdFilm);
        assertNotNull(createdUser);
        assertNotNull(createdUser2);

        filmService.addLike(film.getId(), user.getId());
        filmService.addLike(film.getId(), user2.getId());

        assertEquals(2, film.getFilmLikesCount(), "Некорректное количество лайков");
    }

    @Test
    public void testDeleteLike() {

        User user = createValidUser1();
        User user2 = createValidUser2();

        Film film = createValidFilm();

        User createdUser = userController.create(user);
        User createdUser2 = userController.create(user2);
        Film createdFilm = filmController.create(film);

        assertNotNull(createdFilm);
        assertNotNull(createdUser);
        assertNotNull(createdUser2);

        filmService.addLike(film.getId(), user.getId());
        filmService.addLike(film.getId(), user2.getId());
        filmService.deleteLike(film.getId(), user.getId());

        assertEquals(1, film.getFilmLikesCount(), "Некорректное количество лайков");
    }

}

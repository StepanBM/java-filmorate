package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film checkingIdFilm(long id) {
        return filmStorage.findIdFilm(id).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public User checkingIdUser(long id) {
        return userStorage.findIdUser(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public void addLike(long filmId, long userId) {
        // Проверяем, есть ли фильм и пользователь
        checkingIdFilm(filmId);
        checkingIdUser(userId);

        Set<Long> filmLike = checkingIdUser(userId).getUserLikes();
        // Проверяем, лайкнул ли пользователь фильм ранее
        if (filmLike != null && filmLike.contains(filmId)) {
            throw new NotFoundException("Пользователь лайкнул фильм раннее");
        }
        if (filmLike == null) {
            filmLike = new HashSet<>();
        } else {
            // Создаем изменяемую копию
            filmLike = new HashSet<>(filmLike);
        }
        filmLike.add(filmId);
        checkingIdUser(userId).setUserLikes(filmLike);

        if (checkingIdFilm(filmId).getFilmLikesCount() == null) {
            checkingIdFilm(filmId).setFilmLikesCount(1L);
        } else {
            checkingIdFilm(filmId).setFilmLikesCount(checkingIdFilm(filmId).getFilmLikesCount() + 1L);
        }
    }

    public void deleteLike(long filmId, long userId) {
        // Проверяем, есть ли фильм и пользователь
        checkingIdFilm(filmId);
        checkingIdUser(userId);


            if (checkingIdUser(userId).getId() == userId && checkingIdUser(userId).getUserLikes().contains(filmId)) {
                Set<Long> filmLike = checkingIdUser(userId).getUserLikes();
                filmLike = new HashSet<>(filmLike);
                filmLike.remove(filmId);
                checkingIdUser(userId).setUserLikes(filmLike);

                    if (checkingIdFilm(filmId).getId() == filmId) {
                        checkingIdFilm(filmId).setFilmLikesCount(checkingIdFilm(filmId).getFilmLikesCount() - 1L);
                    }

                return;
            }

        throw new NotFoundException("Данный пользователь не лайкал данный фильм");
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.findAll()
                .stream()
                .sorted(Comparator.comparing(Film::getFilmLikesCount, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(count)
                .collect(Collectors.toList());
    }

}

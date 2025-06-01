package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public void addLike(long filmId, long userId) {
        // Проверяем, есть ли фильм
        if (!(filmStorage.findAll().stream().anyMatch(films -> films.getId()==filmId))) {
            System.out.println("Фильм не найден");
            return;
        }
        for (User user : userStorage.findAll()) {
            if (user.getId() == userId) {
                Set<Long> filmLike = user.getUserLikes();
                // Проверяем, лайкнул ли пользователь фильм ранее
                if (filmLike != null && filmLike.contains(filmId)) {
                    System.out.println("Пользователь уже лайкнул этот фильм.");
                    return; // Выходим из метода, если лайк уже был
                }
                if (filmLike == null) {
                    filmLike = new HashSet<>();
                } else {
                    // Создаем изменяемую копию
                    filmLike = new HashSet<>(filmLike);
                }
                filmLike.add(filmId);
                user.setUserLikes(filmLike);
                break;
            }
        }
        for (Film film : filmStorage.findAll()) {
            if (film.getId() == filmId) {
                if (film.getFilmLikesCount() == null) {
                    film.setFilmLikesCount(1L);
                    break;
                } else {
                    film.setFilmLikesCount(film.getFilmLikesCount() + 1L);
                    break;
                }
            }
        }
    }

    public void deleteLike(long filmId, long userId) {
        // Проверяем, есть ли фильм
        if (!(filmStorage.findAll().stream().anyMatch(films -> films.getId()==filmId) ||
                userStorage.findAll().stream().anyMatch(users -> users.getId()==userId))) {
            System.out.println("Фильм не найден");
            return;
        }
        for (User user : userStorage.findAll()) {
            if (user.getId() == userId && user.getUserLikes().contains(filmId)) {
                Set<Long> filmLike = user.getUserLikes();
                filmLike = new HashSet<>(filmLike);
                filmLike.remove(filmId);
                user.setUserLikes(filmLike);
                for (Film film : filmStorage.findAll()) {
                    if (film.getId() == filmId) {
                        film.setFilmLikesCount(film.getFilmLikesCount() - 1L);
                        break;
                    }
                }
                return;
            }
        }
        System.out.println("Данный пользователь не лайкал данный фильм");
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.findAll()
                .stream()
                .sorted(Comparator.comparing(Film::getFilmLikesCount, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(count)
                .collect(Collectors.toList());
    }

}

//@GetMapping("/posts")
//public List<Post> findAll(
// @RequestParam(defaultValue = "desc") String sort,
// @RequestParam(defaultValue = "0") int from,
// @RequestParam(defaultValue = "10") int size
//) {
// PostService.SortOrder sortOrder = PostService.SortOrder.from(sort);
// if (sortOrder == null) {
// throw new ParameterNotValidException("sort", "Получено: " + sort + " должно быть: ask или desc");
// }
// if (size <= 0) {
// throw new ParameterNotValidException("size", "Размер должен быть больше нуля");
// }
//
// if (from < 0) {
// throw new ParameterNotValidException("from", "Начало выборки должно быть положительным числом");
// }
//
// return postService.findAll(from, size, sortOrder);
//}

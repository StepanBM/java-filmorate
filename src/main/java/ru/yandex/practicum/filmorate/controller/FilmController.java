package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.managerAdapter.ManagerAdapter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    final Gson gson = ManagerAdapter.getGson();

    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody String jsonBody) {

        Film film = gson.fromJson(jsonBody, Film.class);

        try {
            getValidation(film);
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("Создан фильм с id=" + film.getId());
        } catch (Exception e) {
            log.warn("Ошибка при создании фильма");
            throw e;
        }
        return film;
    }

    @PutMapping
    public Film update(@RequestBody String jsonBody) {

        Film newFilm = gson.fromJson(jsonBody, Film.class);

        if (newFilm.getId() == 0) {
            log.error("Попытка обновления фильма без указания id");
            throw new ValidationException("Id должен быть указан");
        }
        try {
            getValidation(newFilm);
            if (films.containsKey(newFilm.getId())) {

                Film oldFilm = films.get(newFilm.getId());

                oldFilm.setName(newFilm.getName());
                oldFilm.setDescription(newFilm.getDescription());
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
                oldFilm.setDuration(newFilm.getDuration());
                log.info("Фильм пользователь с id=" + newFilm.getId());
                return oldFilm;
            } else {
                log.error("Пользователь с таким id не найден");
                throw new ValidationException("Фильм с id =" + newFilm.getId() + " не найден");
            }
        } catch (Exception e) {
            log.warn("Ошибка при обновлении фильма с id=" + newFilm.getId());
            throw e;
        }
    }

    private void getValidation(Film film) {

        long minutes = film.getDuration().toMinutes();
        LocalDate yearBirthMovies = LocalDate.of(1895, 12, 28);

        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Валидация не прошла: название фильма не заполнено");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.info("Описание фильма превышает длину в 200 символов=" + film.getDescription().length());
            throw new ValidationException("Описание фильма превышает длину в 200 символов");
        }
        if (minutes < 0) {
            log.info("Валидация не прошла: продолжительность фильма отрицательная=" + film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        if (film.getReleaseDate().isBefore(yearBirthMovies)) {
            log.info("Валидация не прошла: дата релиза должна быть не раньше 28 декабря 1895 года=" + film.getReleaseDate());
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    // вспомогательный метод для генерации идентификатора
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}

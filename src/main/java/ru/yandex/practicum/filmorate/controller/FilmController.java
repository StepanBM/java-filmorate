package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        try {
            getValidationFilm(film);
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
    public Film update(@Valid @RequestBody Film newFilm) {

        if (newFilm.getId() == 0) {
            log.error("Попытка обновления фильма без указания id");
            throw new ValidationException("Id должен быть указан");
        }

        try {
            getValidationFilm(newFilm);
            if (films.containsKey(newFilm.getId())) {
                Film oldFilm = films.get(newFilm.getId());
                oldFilm.setName(newFilm.getName());
                oldFilm.setDescription(newFilm.getDescription());
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
                oldFilm.setDuration(newFilm.getDuration());
                log.info("Обновлен фильм с id=" + newFilm.getId());
                return oldFilm;
            } else {
                log.error("Фильм с таким id не найден");
                throw new ValidationException("Фильм с id =" + newFilm.getId() + " не найден");
            }
        } catch (Exception e) {
            log.warn("Ошибка при обновлении фильма с id=" + newFilm.getId());
            throw e;
        }
    }

    private void getValidationFilm(Film film) {

        LocalDate yearBirthMovies = LocalDate.of(1895, 12, 28);

        if (film.getReleaseDate().isBefore(yearBirthMovies)) {
            log.info("Валидация не прошла: дата релиза должна быть не раньше 28 декабря 1895 года=" + film.getReleaseDate());
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    // Вспомогательный метод для генерации идентификатора
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}

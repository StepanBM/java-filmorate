package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Override
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение всех фильмов");
        return films.values();
    }

    @Override
    public Film create(Film film) {
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

    @Override
    public Film update(Film newFilm) {
        if (newFilm.getId() == 0) {
            log.error("Попытка обновления фильма без указания id");
            throw new ValidationException("Id должен быть указан");
        }
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
                throw new NotFoundException("Фильм с id =" + newFilm.getId() + " не найден");
            }
    }

    private void getValidationFilm(Film film) {
        LocalDate yearBirthMovies = LocalDate.of(1895, 12, 28);
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

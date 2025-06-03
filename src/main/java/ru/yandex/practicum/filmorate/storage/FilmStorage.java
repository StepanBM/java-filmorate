package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Collection<Film> findAll();

    Film create(@Valid @RequestBody Film film);

    Film update(@Valid @RequestBody Film newFilm);

    Optional<Film> findIdFilm(long id);

}

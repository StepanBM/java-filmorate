package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmInterfaceService {

    List<FilmDto> getFilms();

    FilmDto getFilmById(long filmId);

    FilmDto createFilm(@Valid @RequestBody NewFilmRequest request);

    FilmDto updateFilm(@Valid @RequestBody UpdateFilmRequest request);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> getPopular(int count);
}

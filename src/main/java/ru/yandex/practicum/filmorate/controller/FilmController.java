package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RestController
@Validated
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody NewFilmRequest filmRequest) {
        return filmService.createFilm(filmRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDto> getFilms() {
        return filmService.getFilms();
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest request) {
        return filmService.updateFilm(request);
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto getFilmById(@PathVariable("filmId") @Positive long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable @Positive long id, @PathVariable @Positive long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable @Positive long id, @PathVariable @Positive long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        return filmService.getPopular(count);
    }

}

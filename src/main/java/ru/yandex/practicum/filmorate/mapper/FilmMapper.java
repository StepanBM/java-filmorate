package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());

        if (request.getMpa() != null) {
            RatingMpa ratingMpa = new RatingMpa();
            ratingMpa.setId(request.getMpa().getId());
            film.setRating(ratingMpa);
        }

        if (request.getGenres() != null) {
            List<Genre> genres = request.getGenres().stream()
                    .map(genreRequest -> {
                        Genre genre = new Genre();
                        genre.setId(genreRequest.getId());
                        return genre;
                    })
                    .collect(Collectors.toList());
            film.setGenres(genres);
        }

        film.setFilmLikesCount(0L);
        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());

        dto.setFilmLikesCount(film.getFilmLikesCount());

        if (film.getRating() != null) {
            RatingMpaDto ratingDto = new RatingMpaDto();
            ratingDto.setId(film.getRating().getId());
            ratingDto.setName(film.getRating().getName());
            dto.setMpa(ratingDto);
        } else {
            dto.setMpa(null);
        }

        if (film.getGenres() != null) {
            dto.setGenres(film.getGenres());
        }
        return dto;
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        if (request.hasFilmname()) {
            film.setName(request.getName());
        }
        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }
        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }
        if (request.hasFilDuration()) {
            film.setDuration(request.getDuration());
        }
        if (request.hasRatingId()) {
            if (request.getRatingId() != 0) {
                RatingMpa rating = new RatingMpa();
                rating.setId(request.getRatingId());
                film.setRating(rating);
            } else {
                film.setRating(null);
            }
        }

        return film;
    }

}

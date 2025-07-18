package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class Film {

    private long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание фильма превышает длину в 200 символов")
    @NotNull(message = "Описание фильма не может быть null")
    private String description;

    @NotNull(message = "Дата релиза фильма не может быть null")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    @NotNull(message = "Продолжительность фильма не может быть null")
    private long duration;

    // количество лайков у фильма
    private Long filmLikesCount;

    // рейтинг фильма
    private RatingMpa rating; // например, G, PG, PG-13, R, NC-17

    // список жанров
    private List<Genre> genres;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Long getFilmLikesCount() {
        return filmLikesCount;
    }

    public void setFilmLikesCount(Long filmLikesCount) {
        this.filmLikesCount = filmLikesCount;
    }



    public RatingMpa getRating() {
        return rating;
    }

    public void setRating(RatingMpa rating) {
        this.rating = rating;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
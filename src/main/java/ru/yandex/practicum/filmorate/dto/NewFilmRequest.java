package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class NewFilmRequest {

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

    private NewRatingMpaRequest mpa;

    private List<NewGenreRequest> genres;

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

    public NewRatingMpaRequest getMpa() {
        return mpa;
    }

    public void setMpa(NewRatingMpaRequest mpa) {
        this.mpa = mpa;
    }

    public List<NewGenreRequest> getGenres() {
        return genres;
    }

    public void setGenres(List<NewGenreRequest> genres) {
        this.genres = genres;
    }
}

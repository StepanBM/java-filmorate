package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.List;

public class FilmDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String name;
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate releaseDate;
    private long duration;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RatingMpaDto mpa;

    private Long filmLikesCount;

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

    public RatingMpaDto getMpa() {
        return mpa;
    }

    public void setMpa(RatingMpaDto mpa) {
        this.mpa = mpa;
    }


    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}

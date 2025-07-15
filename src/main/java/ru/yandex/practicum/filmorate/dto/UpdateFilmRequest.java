package ru.yandex.practicum.filmorate.dto;

import java.time.LocalDate;

public class UpdateFilmRequest {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;

    private int ratingId;

    // количество лайков у фильма
    private int genreId;

    public boolean hasFilmname() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasFilDuration() {
        return ! (duration == 0);
    }

    public boolean hasDescription() {
        return ! (description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return ! (releaseDate == null);
    }

    public boolean hasRatingId() {
        return ! (ratingId == 0);
    }

    public boolean hasGenreId() {
        return ! (ratingId == 0);
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
}

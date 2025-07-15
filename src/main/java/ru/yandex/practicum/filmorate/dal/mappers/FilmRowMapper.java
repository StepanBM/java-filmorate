package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));

        Timestamp releaseDate = resultSet.getTimestamp("release_date");
        film.setReleaseDate(LocalDate.from(releaseDate.toLocalDateTime()));

        film.setDuration(resultSet.getLong("duration"));

        film.setFilmLikesCount(resultSet.getLong("filmLikesCount"));

        RatingMpa rating = new RatingMpa();
        rating.setId(resultSet.getInt("rating_id"));
        rating.setName(resultSet.getString("rating_name")); // Добавляем получение имени рейтинга
        film.setRating(rating);

        return film;
    }
}

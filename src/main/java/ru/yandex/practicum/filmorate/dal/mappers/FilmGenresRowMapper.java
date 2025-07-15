package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dto.FilmGenres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class FilmGenresRowMapper implements RowMapper<FilmGenres> {

    @Override
    public FilmGenres mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmGenres filmGenres = new FilmGenres();
        filmGenres.setFilmId(resultSet.getLong("id"));
        filmGenres.setName(resultSet.getString("name"));
        filmGenres.setDescription(resultSet.getString("description"));

        Timestamp releaseDate = resultSet.getTimestamp("release_date");
        filmGenres.setReleaseDate(LocalDate.from(releaseDate.toLocalDateTime()));

        filmGenres.setDuration(resultSet.getLong("duration"));

        filmGenres.setFilmLikesCount(resultSet.getLong("filmLikesCount"));

        filmGenres.setRatingId(resultSet.getInt("rating_id"));
        filmGenres.setRatingName(resultSet.getString("rating_name"));

        filmGenres.setGenreId(resultSet.getInt("genres_id"));
        filmGenres.setGenreName(resultSet.getString("genre_name"));

        return filmGenres;
    }
}

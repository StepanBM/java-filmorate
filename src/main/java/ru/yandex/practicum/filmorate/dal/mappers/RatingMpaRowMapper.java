package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingMpaRowMapper implements RowMapper<RatingMpa> {

    @Override
    public RatingMpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        RatingMpa ratingMpa = new RatingMpa();
        ratingMpa.setId(resultSet.getInt("rating_id"));
        ratingMpa.setName(resultSet.getString("rating_name"));

        return ratingMpa;
    }
}

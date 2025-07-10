package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingMpaRepository extends BaseRepository<RatingMpa> {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating_films WHERE rating_id = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT rf.rating_id, rf.rating_name, " +
                    "FROM rating_films rf " +
                    "GROUP BY rf.rating_id, rf.rating_name";

    public RatingMpaRepository(JdbcTemplate jdbc, RowMapper<RatingMpa> mapper) {
        super(jdbc, mapper);
    }

    public Optional<RatingMpa> findById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<RatingMpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

}

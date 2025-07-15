package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genres_id = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT g.genres_id, g.name, " +
                    "FROM genres g " +
                    "GROUP BY g.genres_id, g.name";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Genre> findById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }
}

package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmGenresRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dto.FilmGenres;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.*;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static final String FIND_ALL_QUERY =
            "SELECT f.id, f.name, f.description, f.release_date, f.duration, r.rating_id, r.rating_name, " +
                    "COUNT(ul.user_id) AS filmLikesCount " +
                    "FROM films f " +
                    "LEFT JOIN rating_films r ON f.rating_id = r.rating_id " +
                    "LEFT JOIN user_likes ul ON f.id = ul.film_id " +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, r.rating_id, r.rating_name";

    private static final String FIND_GENRES_BY_FILM_ID_QUERY =
            "SELECT g.genres_id AS id, g.name AS name FROM genres g " +
                    "JOIN film_genres fg ON g.genres_id = fg.genre_id " +
                    "WHERE fg.film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT f.id, f.name, f.description, f.release_date, f.duration, r.rating_id, r.rating_name, " +
                    "COUNT(ul.user_id) AS filmLikesCount " +
                    "FROM films f " +
                    "LEFT JOIN rating_films r ON f.rating_id = r.rating_id " +
                    "LEFT JOIN user_likes ul ON f.id = ul.film_id " +
                    "WHERE f.id = ? " +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, r.rating_id, r.rating_name";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";

    private static final String ADD_LIKE = "INSERT INTO user_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM user_likes WHERE film_id = ? AND user_id = ?";

    private static final String CHECK_USER_LIKE = "SELECT COUNT(*) FROM user_likes WHERE user_id = ? AND film_id = ?";

    private static final String POPULAR_FILMS = "SELECT f.id, f.name, f.description, f.release_date, f.duration, rf.rating_name, " +
            "(SELECT COUNT(*) FROM user_likes ul WHERE ul.film_id = f.id) AS filmLikesCount " +
            "FROM films f " +
            "LEFT JOIN rating_films rf ON f.rating_id = rf.rating_id " +
            "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, rf.rating_name " +
            "ORDER BY filmLikesCount DESC " +
            "LIMIT ?";

    private static final String GET_ALL_FILMS_WITH_GENRES_QUERY =
            "SELECT f.id as film_id, f.name, f.description, f.release_date, f.duration, f.rating_id," +
                    "     r.rating_name, g.genres_id as genre_id, g.name as genre_name, fl.likes_count AS filmLikesCount " +
                    "FROM films f " +
                    "LEFT JOIN (" +
                    "     SELECT film_id, COUNT(*) AS likes_count " +
                    "     FROM user_likes " +
                    "     GROUP BY film_id " +
                    ") fl ON f.id = fl.film_id " +
                    "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                    "LEFT JOIN genres g ON fg.genre_id = g.genres_id " +
                    "LEFT JOIN rating_films r ON f.rating_id = r.rating_id ";

    private static final String INSERT_FILM_GENRES_BATCH =
            "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> findById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public void addFilmLike(long filmId, long userId) {
        // Проверяем есть ли уже лайк
        Integer count = jdbc.queryForObject(CHECK_USER_LIKE, Integer.class, userId, filmId);
        if (count != null && count > 0) {
            // Лайк уже стоит, ничего не делаем
            return;
        }

        // Вставляем лайк
        jdbc.update(ADD_LIKE, filmId, userId);

    }

    public void deleteFilmLike(long filmId, long userId) {
        Integer count = jdbc.queryForObject(CHECK_USER_LIKE, Integer.class, userId, filmId);
        if (count != null && count > 0) {
            jdbc.update(DELETE_LIKE, filmId, userId);
        }
    }

    public List<FilmGenres> getAllFilmsGenres() {
        return jdbc.query(GET_ALL_FILMS_WITH_GENRES_QUERY, new FilmGenresRowMapper());
    }

    public List<Genre> findGenresByFilmId(long filmId) {
        return jdbc.query(FIND_GENRES_BY_FILM_ID_QUERY, new GenreRowMapper(), filmId);
    }

    public List<Film> getPopularFilms(int count) {

        return jdbc.query(POPULAR_FILMS, rs -> {
            List<Film> films = new ArrayList<>();
            while (rs.next()) {
                Film film = new Film();
                film.setId(rs.getLong("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(rs.getLong("duration"));

                // Установка количества лайков
                Long likesCount = rs.getLong("filmLikesCount");
                film.setFilmLikesCount(likesCount);

                films.add(film);
            }
            return films;
        }, count);
    }

    public Film save(Film film) {

        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                LocalDate.from(film.getReleaseDate()),
                film.getDuration(),
                film.getRating() != null ? film.getRating().getId() : null

        );
        film.setId(id);

        return findById(id).orElse(null);
    }

    public void saveFilmGenres(long filmId, List<Genre> genres) {
        List<Object[]> batchArgs = new ArrayList<>();
        for (Genre genre : genres) {
            // Создаем массив объектов для текущего жанра
            Object[] args = new Object[2];
            // Первый элемент — id фильма
            args[0] = filmId;
            // Второй элемент — id жанра
            args[1] = genre.getId();
            // Добавляем массив аргументов в список batchArgs
            batchArgs.add(args);
        }
        // Выполняем пакетную вставку
        jdbc.batchUpdate(INSERT_FILM_GENRES_BATCH, batchArgs);
    }

    public Film update(Film film) {

        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                LocalDate.from(film.getReleaseDate()),
                film.getDuration(),
                film.getRating() != null ? film.getRating().getId() : null,
                film.getId()
        );
        return film;
    }
}
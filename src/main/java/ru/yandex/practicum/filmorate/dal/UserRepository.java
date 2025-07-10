package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Repository
public class UserRepository extends BaseRepository<User> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(name, login, email, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? WHERE id = ?";
    private static final String INSERT_FRIENDS = "INSERT INTO user_friends (user_id, friend_id, approval) VALUES(?, ?, ?)";
    private static final String FIND_FRIENDS = "SELECT * FROM users "
            + "WHERE users.id IN (SELECT friend_id from user_friends "
            + "WHERE user_id = ?)";
    private static final String DELETE_FRIENDS = "DELETE user_friends WHERE user_id = ? AND friend_id = ?";

    private static final String FIND_COMMON_FRIENDS =
            "SELECT u.* FROM users u " +
                    "JOIN user_friends uf1 ON u.id = uf1.friend_id AND uf1.user_id = ? " +
                    "JOIN user_friends uf2 ON u.id = uf2.friend_id AND uf2.user_id = ?";
    private static final String FIND_USER_LIKE = "SELECT ul.film_id FROM user_likes ul WHERE ul.user_id = ?";
    private static final String FIND_FRIENDS_ID = "SELECT friend_id FROM user_friends WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public void addUserFriends(long userId, long friendId) {
        jdbc.update(INSERT_FRIENDS, userId, friendId, true);
    }

    public void deleteUserFriends(long userId, long friendId) {
        jdbc.update(DELETE_FRIENDS, userId, friendId);
    }

    public List<Long> findLikeByUserId(long userId) {
        return jdbc.query(FIND_USER_LIKE, (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getInt("film_id"));
            return film.getId();
        }, userId);
    }

    public List<User> getlListUserFriends(long userId) {

        Set<Long> friendIds = new HashSet<>();

        List<User> friends = new ArrayList<>();
        SqlRowSet srs =  jdbc.queryForRowSet(FIND_FRIENDS, userId);
        while (srs.next()) {
            User user = new User();
            user.setId(srs.getLong("id"));
            user.setName(srs.getString("name"));
            user.setLogin(srs.getString("login"));
            user.setEmail(srs.getString("email"));
            Timestamp birthday = srs.getTimestamp("birthday");
            user.setBirthday(LocalDate.from(birthday.toLocalDateTime()));
            // добавляем в список
            SqlRowSet rs = jdbc.queryForRowSet(FIND_FRIENDS_ID, user.getId());
            while (rs.next()) {
                friendIds.add(rs.getLong("friend_id"));
            }
            friends.add(user);
            user.setFriends(friendIds);
            user.setUserLikes(new HashSet<>(findLikeByUserId(userId)));

        }

        return friends;
    }

    public List<User> getCommonListUserFriends(long userId, long otherId) {

        List<User> commonFriends = new ArrayList<>();
        Set<Long> friendIds = new HashSet<>();

        SqlRowSet srs = jdbc.queryForRowSet(FIND_COMMON_FRIENDS, userId, otherId);
        while (srs.next()) {
            User user = new User();
            user.setId(srs.getLong("id"));
            user.setName(srs.getString("name"));
            user.setLogin(srs.getString("login"));
            user.setEmail(srs.getString("email"));
            Timestamp birthday = srs.getTimestamp("birthday");
            user.setBirthday(LocalDate.from(birthday.toLocalDateTime()));

            SqlRowSet rs = jdbc.queryForRowSet(FIND_FRIENDS_ID, user.getId());
            while (rs.next()) {
                friendIds.add(rs.getLong("friend_id"));
            }
            commonFriends.add(user);
            user.setFriends(friendIds);
            user.setUserLikes(new HashSet<>(findLikeByUserId(userId)));

        }
        return commonFriends;
    }

    public User save(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                LocalDate.from(user.getBirthday())
        );
        user.setId(id);
        user.setUserLikes(new HashSet<>());

        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                LocalDate.from(user.getBirthday()),
                user.getId()
        );
        return user;
    }
}

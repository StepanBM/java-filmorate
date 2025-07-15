package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({
        UserRepository.class,
        UserService.class,
        UserRowMapper.class,
        UserMapper.class
})
public class UserServiceTest {

    private final UserRepository userRepository;
    private final UserService userService;
    // Добавляем JdbcTemplate для очистки БД
    private final JdbcTemplate jdbcTemplate;

    NewUserRequest newUser1;
    NewUserRequest newUser2;
    NewUserRequest newUser3;

    @Autowired
    public UserServiceTest(UserRepository userRepository, UserService userService, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");

        newUser1 = new NewUserRequest();
        newUser1.setName("Тестовый пользователь №1");
        newUser1.setLogin("Slava0101");
        newUser1.setEmail("mail111@yandex.ru");
        newUser1.setBirthday(LocalDate.of(1997, 11, 15));

        newUser2 = new NewUserRequest();
        newUser2.setName("Тестовый пользователь №2");
        newUser2.setLogin("Step007");
        newUser2.setEmail("mail222@yandex.ru");
        newUser2.setBirthday(LocalDate.of(1999, 10, 9));

        newUser3 = new NewUserRequest();
        newUser3.setName("Тестовый пользователь №3");
        newUser3.setLogin("Jun33");
        newUser3.setEmail("mail333@yandex.ru");
        newUser3.setBirthday(LocalDate.of(2003, 1, 21));

    }

    @Test
    void createUserTest() {

        UserDto createdUser = userService.createUser(newUser3);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getName()).isEqualTo("Тестовый пользователь №3");
        assertThat(createdUser.getLogin()).isEqualTo("Jun33");
        assertThat(createdUser.getEmail()).isEqualTo("mail333@yandex.ru");
        assertThat(createdUser.getBirthday()).isEqualTo("2003-01-21");
    }

    @Test
    void getUsersTest() {
        userService.createUser(newUser1);
        userService.createUser(newUser2);
        userService.createUser(newUser3);
        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(3);
        assertThat(users.get(0).getName()).isEqualTo("Тестовый пользователь №1");
        assertThat(users.get(1).getName()).isEqualTo("Тестовый пользователь №2");
        assertThat(users.get(2).getName()).isEqualTo("Тестовый пользователь №3");
    }

    @Test
    void updateFilmTest() {

        userService.createUser(newUser1);

        User userUpdate = new User();
        userUpdate.setId(1L);
        userUpdate.setName("Обновленное имя пользователя");
        userUpdate.setLogin("Login");
        userUpdate.setEmail("newEmail@yandex.ru");
        userUpdate.setBirthday(LocalDate.of(1999, 9, 23));

        userRepository.update(userUpdate);

        // Получаем обновленный фильм из БД и проверяем, что данные изменились
        Optional<User> updatedUserOptional = userRepository.findById(1);
        assertThat(updatedUserOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getName()).isEqualTo("Обновленное имя пользователя");
                    assertThat(user.getLogin()).isEqualTo("Login");
                    assertThat(user.getEmail()).isEqualTo("newEmail@yandex.ru");
                    assertThat(user.getBirthday()).isEqualTo("1999-09-23");
                });
    }

    @Test
    void addFriendsTest() {
        UserDto userDto1 = userService.createUser(newUser1);
        UserDto userDto2 = userService.createUser(newUser2);
        long newUser1Id = userDto1.getId();
        long newUser2Id = userDto2.getId();

        userService.addFriends(userDto1.getId(), userDto2.getId());

        List<User> users = userService.getlListFriends(newUser1Id);

        assertThat(users).extracting("id").contains(newUser2Id);
    }

    @Test
    void deleteFriendsTest() {
        UserDto userDto1 = userService.createUser(newUser1);
        UserDto userDto2 = userService.createUser(newUser2);
        long newUser1Id = userDto1.getId();
        long newUser2Id = userDto2.getId();

        userService.addFriends(newUser1Id, newUser2Id);

        List<User> users = userService.getlListFriends(newUser1Id);

        assertThat(users).extracting("id").contains(newUser2Id);

        userService.deleteFriends(newUser1Id, newUser2Id);

        List<User> usersDelete = userService.getlListFriends(newUser1Id);

        assertThat(usersDelete).extracting("id").doesNotContain(newUser2Id);
    }

    @Test
    void getCommonListFriendsTest() {
        UserDto userDto1 = userService.createUser(newUser1);
        UserDto userDto2 = userService.createUser(newUser2);
        UserDto userDto3 = userService.createUser(newUser3);
        long newUser1Id = userDto1.getId();
        long newUser2Id = userDto2.getId();
        long newUser3Id = userDto3.getId();

        userService.addFriends(newUser1Id, newUser3Id);
        userService.addFriends(newUser2Id, newUser3Id);

        List<User> commonFriends = userService.getCommonListFriends(newUser1Id, newUser2Id);
        assertThat(commonFriends).extracting("id").contains(newUser3Id);

    }
}

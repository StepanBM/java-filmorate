package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;
    private UserStorage userStorage;
    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    private final UserService userService = new UserService(userStorage);

    @BeforeEach
    public void setUp() {
        userController = new UserController(inMemoryUserStorage, userService);
    }

    // Вспомогательный метод для создания пользователя
    private User createValidUser() {
        User user = new User();
        user.setEmail("BigBos@mail.ru");
        user.setLogin("Arak101010");
        user.setName("Alexander");
        user.setBirthday(LocalDate.of(1997, 7, 3));
        return user;

    }

    // Вспомогательный метод для обновления пользователя
    private User updateValidUser() {
        User updateUser = new User();
        updateUser.setId(1);
        updateUser.setEmail("BumBam@mail.ru");
        updateUser.setLogin("Algon111");
        updateUser.setName("Alex");
        updateUser.setBirthday(LocalDate.of(1997, 7, 5));
        return updateUser;
    }

    @Test
    public void testCreateUser() {

        User user = createValidUser();
        User createdUser = userController.create(user);
        assertNotNull(createdUser);

        Collection<User> collectionUser = userController.findAll();

        assertEquals(1, collectionUser.size(), "Некорректное количество пользователей");
        assertEquals("BigBos@mail.ru", collectionUser.iterator().next().getEmail(), "Некорректный email");
        assertEquals("Arak101010", collectionUser.iterator().next().getLogin(), "Некорректный логин");
        assertEquals("Alexander", collectionUser.iterator().next().getName(), "Некорректное имя");
        assertEquals(createdUser.getBirthday(), collectionUser.iterator().next().getBirthday(), "Некорректная дата рождения");
    }

    @Test
    public void testUpdateUser() {

        User user = createValidUser();
        userController.create(user);

        User newUser = updateValidUser();

        User updatedUser = userController.update(newUser);

        Collection<User> collectionUser = userController.findAll();

        assertEquals(1, collectionUser.size(), "Некорректное количество пользователей");
        assertEquals("BumBam@mail.ru", collectionUser.iterator().next().getEmail(), "Некорректный email");
        assertEquals("Algon111", collectionUser.iterator().next().getLogin(), "Некорректный логин");
        assertEquals("Alex", collectionUser.iterator().next().getName(), "Некорректное имя");
        assertEquals(updatedUser.getBirthday(), collectionUser.iterator().next().getBirthday(), "Некорректная дата рождения");
    }

    @Test
    public void testValidationUserErrorLogin() {

        User user = createValidUser();
        user.setLogin("");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userController.create(user);
                });
        assertEquals("Login не может быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    public void testValidationUserReplacingTheNameWithLogin() {

        User user = createValidUser();
        user.setName("");

        User createdUser = userController.create(user);
        assertNotNull(createdUser);

        Collection<User> collectionUser = userController.findAll();

        assertEquals(1, collectionUser.size(), "Некорректное количество пользователей");
        assertEquals("Arak101010", collectionUser.iterator().next().getName(), "Некорректное имя");
    }

}

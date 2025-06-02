package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {

    private UserController userController;
    private InMemoryUserStorage inMemoryUserStorage;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        inMemoryUserStorage = new InMemoryUserStorage();
        userService = new UserService(inMemoryUserStorage);
        userController = new UserController(inMemoryUserStorage, userService);
    }

    private User createValidUser1() {
        User user = new User();
        user.setEmail("BigBos@mail.ru");
        user.setLogin("Arak101010");
        user.setName("Alexander");
        user.setBirthday(LocalDate.of(1997, 7, 3));
        return user;
    }

    private User createValidUser2() {
        User user2 = new User();
        user2.setEmail("Jumal@mail.ru");
        user2.setLogin("Jum112");
        user2.setName("Sergey");
        user2.setBirthday(LocalDate.of(1999, 7, 3));
        return user2;
    }

    @Test
    public void testAddFriends() {

        User user = createValidUser1();
        User user2 = createValidUser2();

        User createdUser = userController.create(user);
        User createdUser2 = userController.create(user2);

        assertNotNull(createdUser);
        assertNotNull(createdUser2);

        userService.addFriends(user2.getId(), user.getId());

        assertTrue(user.getFriends().contains(2L));
        assertTrue(user2.getFriends().contains(1L));

        assertEquals(Set.of(2L), user.getFriends());
        assertEquals(Set.of(1L), user2.getFriends());
    }

    @Test
    public void testDeleteUserFriends() {

        User user = createValidUser1();
        User user2 = createValidUser2();

        User createdUser = userController.create(user);
        User createdUser2 = userController.create(user2);

        assertNotNull(createdUser);
        assertNotNull(createdUser2);

        userService.addFriends(user2.getId(), user.getId());

        assertTrue(user.getFriends().contains(2L));
        assertTrue(user2.getFriends().contains(1L));

        userService.deleteUserFriends(user.getId(), user2.getId());

        assertTrue(user.getFriends().isEmpty());
        assertTrue(user2.getFriends().isEmpty());
    }

    @Test
    public void testGetlListFriends() {

        User user = createValidUser1();
        User user2 = createValidUser2();

        User createdUser = userController.create(user);
        User createdUser2 = userController.create(user2);

        assertNotNull(createdUser);
        assertNotNull(createdUser2);

        userService.addFriends(user2.getId(), user.getId());

        assertTrue(user.getFriends().contains(2L));
        assertTrue(user2.getFriends().contains(1L));

        List<User> userFriend = userService.getlListFriends(user2.getId());

        assertTrue(userFriend.contains(user));
    }

    @Test
    public void testGetCommonlLstFriends() {

        User user = createValidUser1();
        User user2 = createValidUser2();

        User user3 = new User();
        user3.setEmail("Jumal@mail.ru");
        user3.setLogin("Jum112");
        user3.setName("Sergey");
        user3.setBirthday(LocalDate.of(1999, 7, 3));

        User createdUser = userController.create(user);
        User createdUser2 = userController.create(user2);
        User createdUser3 = userController.create(user3);

        assertNotNull(createdUser);
        assertNotNull(createdUser2);
        assertNotNull(createdUser3);

        userService.addFriends(user2.getId(), user3.getId());
        userService.addFriends(user.getId(), user3.getId());

        List<User> userFriendOther = userService.getCommonlLstFriends(user.getId(), user2.getId());

        assertTrue(userFriendOther.contains(user3));
    }

}

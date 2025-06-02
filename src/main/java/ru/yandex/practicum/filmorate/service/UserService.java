package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(long userId, long friendId) {
        // Проверяем, есть ли пользователь
        boolean existsUser = userStorage.findAll().stream().anyMatch(users -> users.getId() == userId);
        boolean existsUser2 = userStorage.findAll().stream().anyMatch(users -> users.getId() == friendId);
        if (!(existsUser || existsUser2)) {
            throw new NotFoundException("Пользователь не найден");
        }
        for (User user : userStorage.findAll()) {
            if (user.getId() == userId) {
                Set<Long> userFriends = user.getFriends();
                // Проверяем, добавлен ли пользователь раннее в друзья
                if (userFriends != null && userFriends.contains(friendId)) {
                    throw new NotFoundException("Пользователь уже находится в друзьях");
                }
                if (userFriends == null) {
                    userFriends = new HashSet<>();
                } else {
                    // Создаем изменяемую копию
                    userFriends = new HashSet<>(userFriends);
                }
                userFriends.add(friendId);
                user.setFriends(userFriends);
                for (User userFr : userStorage.findAll()) {
                    if (userFr.getId() == friendId) {
                        Set<Long> friendsUser = userFr.getFriends();
                        if (friendsUser == null) {
                            friendsUser = new HashSet<>();
                        } else {
                            // Создаем изменяемую копию
                            friendsUser = new HashSet<>(friendsUser);
                        }
                        friendsUser.add(userId);
                        userFr.setFriends(friendsUser);
                        return;
                    }
                }
            }
        }
    }

    public void deleteUserFriends(long userId, long friendId) {
        // Проверяем, есть ли пользователь
        boolean existsUser = userStorage.findAll().stream().anyMatch(users -> users.getId() == userId);
        boolean existsUser2 = userStorage.findAll().stream().anyMatch(users -> users.getId() == friendId);
        if (!(existsUser || existsUser2)) {
            throw new NotFoundException("Пользователь не найден");
        }
        for (User user : userStorage.findAll()) {
            if (user.getId() == userId && user.getFriends() == null) {
                return;
            }
            if (user.getId() == userId && user.getFriends().contains(friendId)) {
                Set<Long> userFriends = user.getFriends();
                userFriends = new HashSet<>(userFriends);
                userFriends.remove(friendId);
                user.setFriends(userFriends);
                for (User userFr : userStorage.findAll()) {
                    if (userFr.getId() == friendId && userFr.getFriends().contains(userId)) {
                        Set<Long> friendsUser = user.getFriends();
                        friendsUser = new HashSet<>(friendsUser);
                        friendsUser.remove(userId);
                        userFr.setFriends(friendsUser);
                        return;
                    }
                }
            }
        }
 throw new NotFoundException("Данный пользователь не лайкал данный фильм");
    }

    public List<User> getlListFriends(long userId) {
        // Проверяем, есть ли пользователь
        boolean existsUser = userStorage.findAll().stream().anyMatch(users -> users.getId() == userId);
        if (!(existsUser)) {
            throw new NotFoundException("Пользователь не найден");
        }
        List<User> listFrinds = new ArrayList<>();
        for (User userList : userStorage.findAll()) {
            if (userList.getId() == userId) {
                if (userList.getFriends() == null) {
                    return new ArrayList<>();
                }
                for (Long idFrinds: userList.getFriends()) {
                    for (User user : userStorage.findAll()) {
                        if (user.getId() == idFrinds) {
                            listFrinds.add(user);
                        }
                    }
                }
            }
        }

        return listFrinds;
    }

    public List<User> getCommonlLstFriends(long userId, long otherId) {
        // Проверяем, есть ли пользователь
        boolean existsUser = userStorage.findAll().stream().anyMatch(users -> users.getId() == userId);
        boolean existsUser2 = userStorage.findAll().stream().anyMatch(users -> users.getId() == otherId);
        if (!(existsUser || existsUser2)) {
            throw new NotFoundException("Пользователь не найден");
        }
        Set<Long> userFriends = new HashSet<>();
        Set<Long> userFriends2 = new HashSet<>();
        List<Long> otherListId = new ArrayList<>();
        List<User> otherFriends = new ArrayList<>();
        for (User userList : userStorage.findAll()) {
            if (userList.getId() == userId) {
                userFriends = userList.getFriends();
            }
            if (userList.getId() == otherId) {
                userFriends2 = userList.getFriends();
                break;
            }
        }
        // Проходим по второму списку и проверяем, есть ли элементы в первом списке
        for (Long friendId : userFriends2) {
            if (userFriends.contains(friendId)) {
                // Если элемент есть в первом списке, добавляем его в результат
                otherListId.add(friendId);
            }
        }
        for (Long id : otherListId) {
            for (User user : userStorage.findAll()) {
                if (id == user.getId()) {
                    otherFriends.add(user);
                }
            }
        }
        return otherFriends;
    }

}

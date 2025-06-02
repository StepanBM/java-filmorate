package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public Collection<User> findAll() {
        log.info("Получен запрос на получение всех пользователей");
        return users.values();
    }

    @Override
    public User create(User user) {
        getValidationUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан пользователь с id=" + user.getId());
        return user;
    }

    @Override
    public User update(User newUser) {
        if (newUser.getId() == 0) {
            log.error("Попытка обновления пользователя без указания id");
            throw new ValidationException("id должен быть указан"); ////!!!!
        }
        getValidationUser(newUser);
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Обновлен пользователь с id=" + newUser.getId());
            return oldUser;
        } else {
            log.error("Пользователь с таким id не найден");
            throw new ValidationException("Пользователь с id =" + newUser.getId() + " не найден"); ////////!!!!!!
        }

    }

    private void getValidationUser(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Валидация не прошла: некорректный login=" + user.getLogin());
            throw new ValidationException("Login не может быть пустым или содержать пробелы");
        }
        if (user.getName().isBlank()) {
            log.info("Имя не указано, присвоено значение login=" + user.getLogin());
            user.setName(user.getLogin());
        }
    }

    // вспомогательный метод для генерации идентификатора
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}

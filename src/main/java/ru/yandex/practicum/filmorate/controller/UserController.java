package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на получение всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        try {
            getValidationUser(user);
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info("Создан пользователь с id=" + user.getId());
        } catch (Exception e) {
            log.warn("Ошибка при создании пользователя");
            throw e;
        }
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {

        if (newUser.getId() == 0) {
            log.error("Попытка обновления пользователя без указания id");
            throw new ValidationException("Id должен быть указан");
        }

        try {
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
                throw new ValidationException("Пользователь с id =" + newUser.getId() + " не найден");
            }
        } catch (Exception e) {
            log.warn("Ошибка при обновлении пользователя с id=" + newUser.getId());
            throw e;
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

    // Вспомогательный метод для генерации идентификатора
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}

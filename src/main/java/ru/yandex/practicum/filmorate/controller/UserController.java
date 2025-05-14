package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.managerAdapter.ManagerAdapter;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    final Gson gson = ManagerAdapter.getGson();

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на получение всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody String jsonBody) {

        User user = gson.fromJson(jsonBody, User.class);

        try {
            getValidation(user);
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
    public User update(@RequestBody String jsonBody) {

        User newUser = gson.fromJson(jsonBody, User.class);

        if (newUser.getId() == 0) {
            log.error("Попытка обновления пользователя без указания id");
            throw new ValidationException("Id должен быть указан");
        }
        try {
            getValidation(newUser);
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
           throw  e;
        }
    }

    private void getValidation(User user) {

        LocalDate nowTime = LocalDate.now();

        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            log.info("Валидация не прошла: некорректный email=" + user.getEmail());
            throw new ValidationException("Email не может быть пустым или не содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Валидация не прошла: некорректный login=" + user.getLogin());
            throw new ValidationException("Login не может быть пустым или содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя не указано, присвоено значение login=" + user.getLogin());
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(nowTime)) {
            log.info("Валидация не прошла: дата рождения в будущем=" + user.getBirthday());
            throw new ValidationException("Дата рождения не должны быть в будущем");
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

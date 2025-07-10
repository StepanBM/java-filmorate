package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.Set;

//Это класс содержит только те данные, которые необходимы для создания нового пользователя.
//Этот класс используется в запросах на создание и обеспечивает валидацию данных
//в соответствии с бизнес-правилами
public class NewUserRequest {
    private String name;
    private String email;
    private String login;
    @Past(message = "Дата рождения не должны быть в будущем")
    @NotNull(message = "Дата рождения не может быть null")
    private LocalDate birthday;

    private Set<Long> filmLike;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Set<Long> getFilmLike() {
        return filmLike;
    }

    public void setFilmLike(Set<Long> filmLike) {
        this.filmLike = filmLike;
    }
}

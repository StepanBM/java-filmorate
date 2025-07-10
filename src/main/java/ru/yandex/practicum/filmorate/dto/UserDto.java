package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Set;

//Этот класс используется для отображения полной информации о пользователе.
//Этот класс применяется в ответах на запросы данных о пользователе и включает все поля,
//требуемые для работы с пользователями
//@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String name;
    private String login;
    private String email;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate birthday;

    private Set<Long> filmLike;

    // id пользователя, который находится в друзьях
    // private Set<Long> friends;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

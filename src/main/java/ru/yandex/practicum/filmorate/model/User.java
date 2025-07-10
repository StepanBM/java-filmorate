package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

public class User {

    private long id;

    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    private String login;

    @NotNull(message = "Имя пользователя не может быть null")
    private String name;

    @Past(message = "Дата рождения не должны быть в будущем")
    @NotNull(message = "Дата рождения не может быть null")
    private LocalDate birthday;

    // id пользователя, который находится в друзьях
    private Set<Long> friends;

    // id фильма который лайкнул пользователь
    private Set<Long> userLikes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Set<Long> getFriends() {
        return friends;
    }

    public void setFriends(Set<Long> friends) {
        this.friends = friends;
    }

    public Set<Long> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(Set<Long> userLikes) {
        this.userLikes = userLikes;
    }
}
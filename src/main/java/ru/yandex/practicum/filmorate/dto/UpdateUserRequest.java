package ru.yandex.practicum.filmorate.dto;

import java.time.LocalDate;

//Этот класс включает в себя поля, которые могут быть изменены в профиле пользователя.
//При этом каждое поле является необязательным (может быть равно null),
//чтобы позволить частичное обновление данных
public class UpdateUserRequest {
    private long id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;
    // id пользователя, который находится в друзьях
//    private Set<Long> friends;

    public boolean hasUsername() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasEmail() {
        return ! (email == null || email.isBlank());
    }

    public boolean hasLogin() {
        return ! (login == null || login.isBlank());
    }

    public boolean hasBirthday() {
        return ! (birthday == null);
    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}

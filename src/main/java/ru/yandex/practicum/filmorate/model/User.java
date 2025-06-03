package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
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

}
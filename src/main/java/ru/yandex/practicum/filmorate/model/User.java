package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import java.time.LocalDate;

@Data
public class User {

    long id;

    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email не может быть пустым")
    @NotNull(message = "Email не может быть null")
    String email;

    String login;
    String name;

    @Past(message = "Дата рождения не должны быть в будущем")
    LocalDate birthday;

}
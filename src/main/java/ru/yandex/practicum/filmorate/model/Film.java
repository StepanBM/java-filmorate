package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;

@Data
public class Film {

    long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    @NotNull(message = "Название фильма не может быть null")
    String name;

    @Size(max = 200, message = "Описание фильма превышает длину в 200 символов")
    String description;

    LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    long duration;

}
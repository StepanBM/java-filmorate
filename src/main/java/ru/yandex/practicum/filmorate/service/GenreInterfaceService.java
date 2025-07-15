package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.util.List;

public interface GenreInterfaceService {

    GenreDto getGenreById(int id);

    List<GenreDto> getGenre();
}

package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.RatingMpaDto;

import java.util.List;

public interface RatingInterfaceService {

    RatingMpaDto getRatingMpaById(int id);

    List<RatingMpaDto> getRatingMpa();
}
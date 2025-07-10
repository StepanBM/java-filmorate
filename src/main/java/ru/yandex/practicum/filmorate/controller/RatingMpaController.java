package ru.yandex.practicum.filmorate.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.RatingMpaDto;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class RatingMpaController {

    private final RatingMpaService ratingMpaService;

    public RatingMpaController(RatingMpaService ratingMpaService) {
        this.ratingMpaService = ratingMpaService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RatingMpaDto> getRatingMpa() {
        return ratingMpaService.getRatingMpa();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingMpaDto getRatingMpaById(@PathVariable("id") int id) {
        return ratingMpaService.getRatingMpaById(id);
    }

}

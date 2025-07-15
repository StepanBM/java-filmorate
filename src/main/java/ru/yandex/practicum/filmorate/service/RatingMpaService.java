package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.RatingMpaRepository;
import ru.yandex.practicum.filmorate.dto.RatingMpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RatingMpaMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingMpaService implements RatingInterfaceService {

    private final RatingMpaRepository ratingMpaRepository;

    public RatingMpaService(RatingMpaRepository ratingMpaRepository) {
        this.ratingMpaRepository = ratingMpaRepository;
    }

    @Override
    public RatingMpaDto getRatingMpaById(int id) {
        return ratingMpaRepository.findById(id)
                .map(RatingMpaMapper::mapToRatingMpaDto)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден с ID: " + id));
    }

    @Override
    public List<RatingMpaDto> getRatingMpa() {
        return ratingMpaRepository.findAll()
                .stream()
                .map(RatingMpaMapper::mapToRatingMpaDto)
                .collect(Collectors.toList());
    }

}

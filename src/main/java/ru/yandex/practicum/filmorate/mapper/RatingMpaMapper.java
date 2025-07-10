package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.NewRatingMpaRequest;
import ru.yandex.practicum.filmorate.dto.RatingMpaDto;
import ru.yandex.practicum.filmorate.model.RatingMpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RatingMpaMapper {

    public static RatingMpa mapToRatingMpa(NewRatingMpaRequest request) {
        RatingMpa ratingMpa = new RatingMpa();
        ratingMpa.setId(request.getId());
        // ratingMpa.setName(request.getName());

        return ratingMpa;
    }

    public static RatingMpaDto mapToRatingMpaDto(RatingMpa ratingMpa) {
        RatingMpaDto dto = new RatingMpaDto();
        dto.setId(ratingMpa.getId());
        dto.setName(ratingMpa.getName());

        return dto;
    }
}
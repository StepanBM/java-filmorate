package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService implements FilmInterfaceService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final UserInterfaceService userService;
    private final GenreInterfaceService genreService;
    private final RatingInterfaceService ratingMpaService;

    public FilmService(FilmRepository filmRepository, UserRepository userRepository, UserInterfaceService userService, GenreInterfaceService genreService, RatingInterfaceService ratingMpaService) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.genreService = genreService;
        this.ratingMpaService = ratingMpaService;
    }

    @Override
    public FilmDto createFilm(NewFilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);
        getValidationFilm(film);
        if (request.getMpa() != null) {
            ratingMpaService.getRatingMpaById(film.getRating().getId());
        }
        film = filmRepository.save(film);

        // Обработка жанров
        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            List<Genre> genres = new ArrayList<>();
            for (NewGenreRequest genreRequest : request.getGenres()) {
                GenreDto genreDto = genreService.getGenreById(genreRequest.getId());
                Genre genre = GenreMapper.mapToGenre(genreDto);
                genres.add(genre);
            }

            // Удаляем дубликаты по id
            Set<Integer> setId = new HashSet<>();
            List<Genre> uniqueGenres = new ArrayList<>();
            for (Genre g : genres) {
                if (!setId.contains(g.getId())) {
                    setId.add(g.getId());
                    uniqueGenres.add(g);
                }
            }

            filmRepository.saveFilmGenres(film.getId(), uniqueGenres);
            film.setGenres(uniqueGenres);
        }

        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto getFilmById(long filmId) {

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + filmId));

        // Получаем жанры фильма
        List<Genre> genres = filmRepository.findGenresByFilmId(filmId);
        film.setGenres(genres);

        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public List<FilmDto> getFilms() {

        List<Film> films = filmRepository.findAll();
        return films.stream()
                .map(film -> {
                    // Получаем жанры для каждого фильма
                    List<Genre> genres = filmRepository.findGenresByFilmId(film.getId());
                    // Устанавливаем жанры в объект Film
                    film.setGenres(genres);
                    return film;
                })
                // Преобразуем Film в FilmDto
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public FilmDto updateFilm(UpdateFilmRequest request) {
        Film updatedFilm = filmRepository.findById(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, request))
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        updatedFilm = filmRepository.update(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public Film checkingIdFilm(long id) {
        return filmRepository.findById(id).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public User checkingIdUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Override
    public void addLike(long filmId, long userId) {
        // Проверяем, есть ли фильм и пользователь
        checkingIdFilm(filmId);
        checkingIdUser(userId);

        filmRepository.addFilmLike(filmId, userId);

        UserDto user = userService.getUserById(userId);
        if (user != null) {
            if (user.getFilmLike() == null) {
                user.setFilmLike(new HashSet<>());
            }
            user.getFilmLike().add(filmId);
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        // Проверяем, есть ли фильм и пользователь
        checkingIdFilm(filmId);
        checkingIdUser(userId);

        filmRepository.deleteFilmLike(filmId, userId);

    }

    @Override
    public List<Film> getPopular(int count) {
        return filmRepository.getPopularFilms(count);
    }

    private void getValidationFilm(Film film) {
        LocalDate yearBirthMovies = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(yearBirthMovies)) {
            //  log.info("Валидация не прошла: дата релиза должна быть не раньше 28 декабря 1895 года=" + film.getReleaseDate());
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

}

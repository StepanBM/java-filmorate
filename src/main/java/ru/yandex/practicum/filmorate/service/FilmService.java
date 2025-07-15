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
import ru.yandex.practicum.filmorate.model.RatingMpa;
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
            List<GenreDto> genresDtoList = genreService.getGenre();
            // Создаем пустую карту для хранения жанров по ID
            Map<Integer, GenreDto> genreMap = new HashMap<>();
            // Проходим по всем жанрам из списка allGenresDto
            for (GenreDto g : genresDtoList) {
                // Получаем ID жанра
                Integer id = g.getId();
                // Помещаем жанр в карту с ключом - его ID
                genreMap.put(id, g);
            }

            List<Genre> genres = new ArrayList<>();
            for (NewGenreRequest genreRequest : request.getGenres()) {
                GenreDto genreDto = genreMap.get(genreRequest.getId());
                if (genreDto != null) {
                    Genre genre = GenreMapper.mapToGenre(genreDto);
                    genres.add(genre);
                } else {
                    throw new NotFoundException("Жанр с данным id не найден");
                }
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
                .orElseThrow(() -> new NotFoundException("Фильм не найден с id: " + filmId));

        // Получаем жанры фильма
        List<Genre> genres = filmRepository.findGenresByFilmId(filmId);
        film.setGenres(genres);

        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public List<FilmDto> getFilms() {
        // Получаем список всех фильмов
        List<FilmGenres> rawData = filmRepository.getAllFilmsGenres();

        Map<Long, Film> filmsMap = new HashMap<>();
        Map<Long, List<Genre>> genresMap = new HashMap<>();

        for (FilmGenres item : rawData) {
            long filmId = item.getFilmId();
            // Проверяем, есть ли уже фильм с этим id в карте
            Film film = filmsMap.get(filmId);

            // Если фильма нет, создаем новый и заполняем его свойствами
            if (film == null) {
                Film newFilm = new Film();
                newFilm.setId(item.getFilmId());
                newFilm.setName(item.getName());
                newFilm.setDescription(item.getDescription());
                newFilm.setReleaseDate(item.getReleaseDate());
                newFilm.setDuration(item.getDuration());

                newFilm.setFilmLikesCount(item.getFilmLikesCount());

                RatingMpa rating = new RatingMpa();
                rating.setId(item.getRatingId());
                rating.setName(item.getRatingName());
                newFilm.setRating(rating);

                // Добавляем новый фильм в карту
                filmsMap.put(filmId, newFilm);
                film = newFilm;
            }

            // Проверка наличия жанра и заполнение списка жанров
            if (item.getGenreId() != null && item.getGenreName() != null) {
                Genre genre = new Genre();
                genre.setId(item.getGenreId());
                genre.setName(item.getGenreName());

                // Проверяем, есть ли уже список жанров для данного filmId
                List<Genre> genresList = genresMap.get(filmId);

                // Если списка нет, создаем новый и кладем в карту
                if (genresList == null) {
                    genresList = new ArrayList<>();
                    genresMap.put(filmId, genresList);
                }

                // Добавляем жанр в список
                genresList.add(genre);
            }
        }

        // Собираем финальный список фильмов
        List<Film> films = new ArrayList<>(filmsMap.values());

        // Присваиваем каждому фильму соответствующий список жанров
        for (Film film : films) {
            // Получаем список жанров для данного фильма по его ID
            List<Genre> genresFilm = genresMap.get(film.getId());

            // Если список равен null, создаем пустой список
            if (genresFilm == null) {
                genresFilm = new ArrayList<>();
            }
            film.setGenres(genresFilm);
        }

        // Конвертируем список фильмов в DTO
        return films.stream()
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
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

}

package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserInterfaceService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(NewUserRequest request) {

        User user = UserMapper.mapToUser(request);
        getValidationUser(user);

        user = userRepository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUserById(long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + userId));

        // Получаем жанры фильма
        List<Long> likes = userRepository.findLikeByUserId(userId);
        user.setUserLikes(new HashSet<>(likes));

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    List<Long> userLike = userRepository.findLikeByUserId(user.getId());
                    user.setUserLikes(new HashSet<>(userLike));
                    return user;
                }).map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UpdateUserRequest request) {
        User updatedUser = userRepository.findById(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        updatedUser = userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public void addFriends(long userId, long friendId) {
        // Проверяем, есть ли пользователь
        checkingIdUser(userId);
        checkingIdUser(friendId);

        userRepository.addUserFriends(userId, friendId);

    }

    @Override
    public List<User> getlListFriends(long userId) {
        // Проверяем, есть ли пользователь
        checkingIdUser(userId);

        List<User> listFrinds = userRepository.getlListUserFriends(userId);

        return listFrinds;
    }

    @Override
    public void deleteFriends(long userId, long friendId) {
        // Проверяем, есть ли пользователь
        checkingIdUser(userId);
        checkingIdUser(friendId);

        userRepository.deleteUserFriends(userId, friendId);
    }


    public User checkingIdUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }


    @Override
    public List<User> getCommonListFriends(long userId, long otherId) {
        // Проверяем, есть ли пользователь
        checkingIdUser(userId);
        checkingIdUser(otherId);

        List<User> otherFriends = userRepository.getCommonListUserFriends(userId, otherId);

        return otherFriends;
    }

    private void getValidationUser(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login не может быть пустым или содержать пробелы");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}

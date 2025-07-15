package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserInterfaceService {

    UserDto createUser(@Valid @RequestBody NewUserRequest request);

    UserDto getUserById(long userId);

    List<UserDto> getUsers();

    UserDto updateUser(@Valid @RequestBody UpdateUserRequest request);

    void addFriends(long userId, long friendId);

    List<User> getlListFriends(long userId);

    void deleteFriends(long userId, long friendId);

    List<User> getCommonListFriends(long userId, long otherId);
}
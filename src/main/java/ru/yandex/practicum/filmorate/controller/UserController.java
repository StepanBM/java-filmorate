package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable("userId") @Positive long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable @Positive long id, @PathVariable @Positive long friendId) {
        userService.addFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getlLstFriends(@PathVariable @Positive long id) {
        return userService.getlListFriends(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteUserFriends(@PathVariable @Positive long id, @PathVariable @Positive long friendId) {
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonlLstFriends(@PathVariable @Positive long id, @PathVariable @Positive long otherId) {
        return userService.getCommonListFriends(id, otherId);
    }

}

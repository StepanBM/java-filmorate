package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

//Вся логика преобразования объектов типа User в тип UserDto
//и обратно описана в классе UserMapper:
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static User mapToUser(NewUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setLogin(request.getLogin());
        user.setBirthday(request.getBirthday());
        user.setUserLikes(request.getFilmLike());

        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setLogin(user.getLogin());
        dto.setBirthday(user.getBirthday());
        dto.setFilmLike(user.getUserLikes());
        return dto;
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasUsername()) {
            user.setName(request.getName());
        }
        if (request.hasLogin()) {
            user.setLogin(request.getLogin());
        }
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasBirthday()) {
            user.setBirthday(request.getBirthday());
        }
        return user;
    }
}
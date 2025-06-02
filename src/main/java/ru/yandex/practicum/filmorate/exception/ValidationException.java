package ru.yandex.practicum.filmorate.exception;

// IllegalArgumentException
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

//

// @ExceptionHandler(DuplicatedDataException.class)

// @ResponseStatus(HttpStatus.CONFLICT)

// public ErrorResponse handleDuplicatedData(DuplicatedDataException e) {

// return new ErrorResponse(e.getMessage());

// }

//

// @ExceptionHandler(ConditionsNotMetException.class)

// @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)

// public ErrorResponse handleConditionsNotMet(ConditionsNotMetException e) {

// return new ErrorResponse(e.getMessage());

// }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterNotValid(ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(Throwable e) {
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }

}

package ru.yandex.practicum.filmorate.exception;

// RuntimeException
public class ValidationException extends IllegalArgumentException {

    private String parameter;
    private String reason;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String parameter, String reason) {
        this.parameter = parameter;
        this.reason = reason;
    }

    public String getParameter() {
        return parameter;
    }

    public String getReason() {
        return reason;
    }

}

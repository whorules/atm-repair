package ru.korovko.atm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot parse date because of incorrect format")
public class CannotParseDateException extends RuntimeException {

    public CannotParseDateException(String message, Throwable cause) {
        super(message, cause);
    }
}

package ru.korovko.atm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "File extension is incorrect")
public class IncorrectFileExtensionException extends RuntimeException {

    public IncorrectFileExtensionException(String message) {
        super(message);
    }
}

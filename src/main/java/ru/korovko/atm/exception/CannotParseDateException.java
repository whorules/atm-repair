package ru.korovko.atm.exception;

public class CannotParseDateException extends RuntimeException {

    public CannotParseDateException(String message, Throwable cause) {
        super(message, cause);
    }
}

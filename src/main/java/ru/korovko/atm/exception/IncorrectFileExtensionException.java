package ru.korovko.atm.exception;

public class IncorrectFileExtensionException extends RuntimeException {

    public IncorrectFileExtensionException(String message) {
        super(message);
    }
}

package ru.korovko.atm.exception;

public class IncorrectFileFormatException extends RuntimeException {

    public IncorrectFileFormatException(String message) {
        super(message);
    }
}

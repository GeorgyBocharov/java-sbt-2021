package ru.sbt.service.error;

public class ClientParsingException extends RuntimeException {
    public ClientParsingException(String message) {
        super(message);
    }
}

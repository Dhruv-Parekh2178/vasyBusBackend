package com.app.vasyBus.exception;

public class BusAlreadyExistsException extends RuntimeException {
    public BusAlreadyExistsException(String message) {
        super(message);
    }
}

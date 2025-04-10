package com.ahnis.servaia.user.exception;

public class UsernameOrEmailAlreadyExistsException extends RuntimeException {
    public UsernameOrEmailAlreadyExistsException(String message) {
        super(message);
    }
}

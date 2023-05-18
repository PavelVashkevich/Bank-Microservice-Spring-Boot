package com.github.pavelvashkevich.bankmicroservice.exception;

public class NotValidRequestException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NotValidRequestException(String message) {
        super(message);
    }
}

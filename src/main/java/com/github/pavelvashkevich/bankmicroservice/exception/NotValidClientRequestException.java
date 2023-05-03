package com.github.pavelvashkevich.bankmicroservice.exception;

public class NotValidClientRequestException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NotValidClientRequestException(String message) {
        super(message);
    }
}

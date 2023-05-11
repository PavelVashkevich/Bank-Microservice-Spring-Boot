package com.github.pavelvashkevich.bankmicroservice.exception;

public class RemoteAPIAccessException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public RemoteAPIAccessException(String message) {
        super(message);
    }
}

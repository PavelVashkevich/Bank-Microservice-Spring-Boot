package com.github.pavelvashkevich.bankmicroservice.exception;

public class BankAccountExistException extends RuntimeException {

    public BankAccountExistException(String message) {
        super(message);
    }
}

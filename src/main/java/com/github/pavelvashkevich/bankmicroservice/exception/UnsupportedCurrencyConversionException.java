package com.github.pavelvashkevich.bankmicroservice.exception;

public class UnsupportedCurrencyConversionException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public UnsupportedCurrencyConversionException(String message) {
        super(message);
    }
}

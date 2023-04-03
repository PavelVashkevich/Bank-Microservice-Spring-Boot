package com.github.pavelvashkevich.bankmicroservice.model.types;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private long timestamp;
}

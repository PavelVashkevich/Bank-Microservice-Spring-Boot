package com.github.pavelvashkevich.bankmicroservice.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ErrorResponse {
    @Getter
    private String message;
    private long timestamp;
}

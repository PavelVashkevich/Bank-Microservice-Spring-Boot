package com.github.pavelvashkevich.bankmicroservice.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private String message;
    private long timestamp;
}

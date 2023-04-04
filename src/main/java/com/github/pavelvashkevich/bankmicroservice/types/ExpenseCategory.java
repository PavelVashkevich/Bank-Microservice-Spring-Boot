package com.github.pavelvashkevich.bankmicroservice.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ExpenseCategory {
    @JsonProperty(value = "product")
    PRODUCT,
    @JsonProperty(value = "service")
    SERVICE
}

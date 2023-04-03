package com.github.pavelvashkevich.bankmicroservice.model.types.enumerators;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ExpenseCategory {

    @JsonProperty("product")
    PRODUCT,
    @JsonProperty("service")
    SERVICE
}
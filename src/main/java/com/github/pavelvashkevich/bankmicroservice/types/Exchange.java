package com.github.pavelvashkevich.bankmicroservice.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Exchange {

    USD_TO_RUB("USD/RUB"),
    USD_TO_KZT("USD/KZT");

    @Getter
    private final String symbol;
}
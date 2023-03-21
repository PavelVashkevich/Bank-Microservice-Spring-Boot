package com.github.pavelvashkevich.bankmicroservice.types;

import lombok.Getter;

public enum Exchange {

    USD_TO_RUB("USD/RUB"),
    USD_TO_KZT("USD/KZT");

    @Getter
    private final String symbol;

    Exchange(String symbol) {
        this.symbol = symbol;
    }
}
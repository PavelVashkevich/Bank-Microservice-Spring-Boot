package com.github.pavelvashkevich.bankmicroservice.model.types.enumerators;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Exchange {

    USD_TO_RUB("USD/RUB"),
    USD_TO_KZT("USD/KZT");

    @Getter
    private final String symbol;
}
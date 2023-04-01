package com.github.pavelvashkevich.bankmicroservice.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExpenseCategory {
    PRODUCT("product"),
    SERVICE("service");

    @Getter
    private final String name;
}

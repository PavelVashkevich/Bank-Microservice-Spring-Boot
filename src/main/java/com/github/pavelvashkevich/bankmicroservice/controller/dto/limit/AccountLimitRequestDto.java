package com.github.pavelvashkevich.bankmicroservice.controller.dto.limit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.pavelvashkevich.bankmicroservice.types.ExpenseCategory;

public class AccountLimitRequestDto {

    @JsonProperty("expense_category")
    private ExpenseCategory expenseCategory;

}

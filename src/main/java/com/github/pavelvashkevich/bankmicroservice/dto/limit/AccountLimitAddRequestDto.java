package com.github.pavelvashkevich.bankmicroservice.dto.limit;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pavelvashkevich.bankmicroservice.model.types.annotations.ValueOfEnum;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.ExpenseCategory;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountLimitAddRequestDto {
    @Valid
    @NotNull
    private BankAccountLimitDto bankAccount;
    @PositiveOrZero(message = "Sum must be equal of higher than 0")
    @NotNull
    private BigDecimal limitSum;
    @NotNull
    @ValueOfEnum(enumClass = Currency.class)
    private String limitCurrencyShortname;
    @NotNull
    @ValueOfEnum(enumClass = ExpenseCategory.class)
    private String limitExpenseCategory;
}

package com.github.pavelvashkevich.bankmicroservice.dto.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankAccountDto {
    @NotNull
    @Digits(integer = 10, fraction = 0, message = "'account_number' must contain 10 digits")
    private Integer accountNumber;
}

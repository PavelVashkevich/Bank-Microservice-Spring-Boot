package com.github.pavelvashkevich.bankmicroservice.dto.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.pavelvashkevich.bankmicroservice.model.types.annotations.AccountNumberConstraint;
import com.github.pavelvashkevich.bankmicroservice.model.types.annotations.UniqueBankAccountNumber;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankAccountClientDto {
    @NotNull
    @UniqueBankAccountNumber(value = true)
    @AccountNumberConstraint(message = "Size of the account number must be 10 digits")
    private Long accountNumber;
}

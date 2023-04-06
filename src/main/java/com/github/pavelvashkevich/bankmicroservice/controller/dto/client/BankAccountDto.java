package com.github.pavelvashkevich.bankmicroservice.controller.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BankAccountDto {
    @NotNull
    @Digits(integer = 10, fraction = 0, message = "'account_number' must contain 10 digits")
    @JsonProperty(value = "account_number")
    private Integer accountNumber;
}

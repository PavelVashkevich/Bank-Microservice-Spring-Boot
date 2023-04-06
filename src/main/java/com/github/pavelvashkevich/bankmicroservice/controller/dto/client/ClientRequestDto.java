package com.github.pavelvashkevich.bankmicroservice.controller.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ClientRequestDto {
    @NotNull
    @JsonProperty(value = "bank_account")
    private BankAccountDto bankAccountDTO;
}

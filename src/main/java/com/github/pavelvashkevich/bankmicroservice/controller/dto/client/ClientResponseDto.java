package com.github.pavelvashkevich.bankmicroservice.controller.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseDto {

    private Long id;
    @JsonProperty("bank_account")
    private BankAccountDto bankAccount;
}

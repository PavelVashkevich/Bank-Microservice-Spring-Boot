package com.github.pavelvashkevich.bankmicroservice.dto.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientResponseDto {

    private Long id;
    private BankAccountDto bankAccount;
}

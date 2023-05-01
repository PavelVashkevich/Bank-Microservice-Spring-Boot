package com.github.pavelvashkevich.bankmicroservice.validator;

import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.model.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.repository.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
@AllArgsConstructor
public class ClientDtoValidator implements Validator {
    private static final String BANK_ACCOUNT_TAKEN_MSG = "Bank account with this number is already exist.";

    private BankAccountRepository bankAccountRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return BankAccount.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ClientRequestDto clientRequestDto = (ClientRequestDto) obj;
        if (bankAccountRepository.findByAccountNumber(clientRequestDto.getBankAccount().getAccountNumber()).isPresent()) {
            errors.reject("accountNumber", BANK_ACCOUNT_TAKEN_MSG);
        }
    }
}

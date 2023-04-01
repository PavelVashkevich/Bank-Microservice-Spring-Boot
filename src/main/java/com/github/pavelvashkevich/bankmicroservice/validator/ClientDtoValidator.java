package com.github.pavelvashkevich.bankmicroservice.validator;

import com.github.pavelvashkevich.bankmicroservice.controller.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.model.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.repository.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class ClientDtoValidator implements Validator {

    private BankAccountRepository bankAccountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return BankAccount.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ClientRequestDto clientRequestDto = (ClientRequestDto) obj;
        if (bankAccountRepository.findByAccountNumber(clientRequestDto.getBankAccountDTO().getAccountNumber()).isPresent()) {
            errors.reject("accountNumber", "Bank account with this number is already exist");
        }
    }
}

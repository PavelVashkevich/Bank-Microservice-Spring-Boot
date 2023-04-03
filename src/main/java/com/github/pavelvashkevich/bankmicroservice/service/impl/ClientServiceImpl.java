package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.controller.dto.client.BankAccountDto;
import com.github.pavelvashkevich.bankmicroservice.controller.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.controller.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.exception.NoDataFoundException;
import com.github.pavelvashkevich.bankmicroservice.handler.NewBankAccountLimitHandler;
import com.github.pavelvashkevich.bankmicroservice.model.AccountLimit;
import com.github.pavelvashkevich.bankmicroservice.model.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.Client;
import com.github.pavelvashkevich.bankmicroservice.repository.ClientRepository;
import com.github.pavelvashkevich.bankmicroservice.service.ClientService;
import com.github.pavelvashkevich.bankmicroservice.util.MessageResourceBundler;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final NewBankAccountLimitHandler newBankAccountLimitHandler;

    @Override
    @Transactional
    public ClientResponseDto save(ClientRequestDto clientRequestDto) {
        BankAccount bankAccount = modelMapper.map(clientRequestDto.getBankAccountDTO(), BankAccount.class);
        Client client = modelMapper.map(clientRequestDto, Client.class);
        client.setBankAccount(bankAccount);
        bankAccount.setClient(client);
        enrichClient(client);
        Client createdClient = clientRepository.save(client);
        newBankAccountLimitHandler.addAccountLimitToTheNewBankAccount(bankAccount);
        return modelMapper.map(createdClient, ClientResponseDto.class);
    }

    @Override
    @Transactional
    public void delete(long id) {
        if (clientRepository.existsById(id))
            clientRepository.deleteById(id);
        throw new NoDataFoundException(String.format(MessageResourceBundler.NO_CLIENT_WITH_ID_MSG, id));
    }

    @Override
    public List<Client> findByRegistrationDate(LocalDate registrationDate) {
        return clientRepository.findByRegistrationDate(registrationDate);
    }

    @Override
    public ClientResponseDto getOne(long id) {
        if (clientRepository.existsById(id)) {
            Client client = clientRepository.findById(id).get();
            ClientResponseDto clientResponseDto = modelMapper.map(client, ClientResponseDto.class);
            BankAccountDto bankAccountDto = modelMapper.map(client.getBankAccount(), BankAccountDto.class);
            clientResponseDto.setBankAccount(bankAccountDto);
            return clientResponseDto;
        }
        throw new NoDataFoundException(String.format(MessageResourceBundler.NO_CLIENT_WITH_ID_MSG, id));
    }

    private void enrichClient(Client client) {
        client.setRegistrationDate(LocalDate.now());
    }
}
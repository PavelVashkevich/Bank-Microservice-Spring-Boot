package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.dto.client.BankAccountDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.exception.NoDataFoundException;
import com.github.pavelvashkevich.bankmicroservice.model.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.Client;
import com.github.pavelvashkevich.bankmicroservice.repository.ClientRepository;
import com.github.pavelvashkevich.bankmicroservice.service.ClientService;
import com.github.pavelvashkevich.bankmicroservice.util.NewBankAccountLimitUtil;
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

    private static final String NO_CLIENT_WITH_ID_MSG = "Client with %d doesn't exist";
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final NewBankAccountLimitUtil newBankAccountLimitUtil;

    @Override
    @Transactional
    public ClientResponseDto save(ClientRequestDto clientRequestDto) {
        BankAccount bankAccount = modelMapper.map(clientRequestDto.getBankAccountDTO(), BankAccount.class);
        Client client = modelMapper.map(clientRequestDto, Client.class);
        client.setBankAccount(bankAccount);
        bankAccount.setClient(client);
        enrichClient(client);
        Client createdClient = clientRepository.save(client);
        newBankAccountLimitUtil.addAccountLimitToTheNewBankAccount(bankAccount);
        return modelMapper.map(createdClient, ClientResponseDto.class);
    }

    @Override
    @Transactional
    public void delete(long id) {
        if (clientRepository.existsById(id))
            clientRepository.deleteById(id);
        // TODO log.info
    }

    @Override
    public List<Client> findByRegistrationDate(LocalDate registrationDate) {
        return clientRepository.findByRegistrationDate(registrationDate);
    }

    @Override
    public ClientResponseDto getClient(long id) {
        if (clientRepository.existsById(id)) {
            Client client = clientRepository.findById(id).get();
            ClientResponseDto clientResponseDto = modelMapper.map(client, ClientResponseDto.class);
            BankAccountDto bankAccountDto = modelMapper.map(client.getBankAccount(), BankAccountDto.class);
            clientResponseDto.setBankAccount(bankAccountDto);
            return clientResponseDto;
        }
        throw new NoDataFoundException(String.format(NO_CLIENT_WITH_ID_MSG, id));
    }

    private void enrichClient(Client client) {
        client.setRegistrationDate(LocalDate.now());
    }
}
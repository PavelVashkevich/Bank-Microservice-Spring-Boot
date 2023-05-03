package com.github.pavelvashkevich.bankmicroservice.mapper;

import com.github.pavelvashkevich.bankmicroservice.dto.client.BankAccountDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.model.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.Client;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientModelMapper {
    private final ModelMapper modelMapper;

    public Client mapRequestDtoToClient(ClientRequestDto clientRequestDto) {
        BankAccount bankAccount = modelMapper.map(clientRequestDto.getBankAccount(), BankAccount.class);
        Client client = modelMapper.map(clientRequestDto, Client.class);
        client.setBankAccount(bankAccount);
        bankAccount.setClient(client);
        return client;
    }

    public ClientResponseDto mapClientToResponseDto(Client client) {
        ClientResponseDto clientResponseDto = modelMapper.map(client, ClientResponseDto.class);
        BankAccountDto bankAccountDto = modelMapper.map(client.getBankAccount(), BankAccountDto.class);
        clientResponseDto.setBankAccount(bankAccountDto);
        return clientResponseDto;
    }
}

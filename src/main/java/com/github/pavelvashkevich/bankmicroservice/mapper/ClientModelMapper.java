package com.github.pavelvashkevich.bankmicroservice.mapper;

import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.Client;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
        return this.modelMapper.map(client, ClientResponseDto.class);
    }

    @PostConstruct
    private void initClientTypeMap() {
        TypeMap<Client, ClientResponseDto> propertyMapClientToClientResponseDto
                = modelMapper.createTypeMap(Client.class, ClientResponseDto.class);
        propertyMapClientToClientResponseDto.addMapping(Client::getBankAccount, ClientResponseDto::setBankAccount);
    }
}

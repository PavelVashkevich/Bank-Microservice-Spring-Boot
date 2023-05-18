package com.github.pavelvashkevich.bankmicroservice.mapper;

import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.Client;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientModelMapper {
    private final ModelMapper modelMapper;

    public Client mapRequestDtoToClient(ClientRequestDto clientRequestDto) {
        TypeMap<ClientRequestDto, Client> propertyMap
                = this.modelMapper.createTypeMap(ClientRequestDto.class, Client.class);
        propertyMap.addMapping(ClientRequestDto::getBankAccount, Client::setBankAccount);
        return this.modelMapper.map(clientRequestDto, Client.class);
    }

    public ClientResponseDto mapClientToResponseDto(Client client) {
        TypeMap<Client, ClientResponseDto> propertyMap
                = this.modelMapper.createTypeMap(Client.class, ClientResponseDto.class);
        propertyMap.addMapping(Client::getBankAccount, ClientResponseDto::setBankAccount);
        return this.modelMapper.map(client, ClientResponseDto.class);
    }
}

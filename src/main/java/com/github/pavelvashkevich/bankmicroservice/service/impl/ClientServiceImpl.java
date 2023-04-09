package com.github.pavelvashkevich.bankmicroservice.service.impl;

import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.exception.NoDataFoundException;
import com.github.pavelvashkevich.bankmicroservice.mapper.ClientModelMapper;
import com.github.pavelvashkevich.bankmicroservice.model.Client;
import com.github.pavelvashkevich.bankmicroservice.repository.ClientRepository;
import com.github.pavelvashkevich.bankmicroservice.service.ClientService;
import com.github.pavelvashkevich.bankmicroservice.util.NewBankAccountLimitUtil;
import lombok.AllArgsConstructor;
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
    private final ClientModelMapper clientModelMapper;
    private final NewBankAccountLimitUtil newBankAccountLimitUtil;

    @Override
    @Transactional
    public ClientResponseDto save(ClientRequestDto clientRequestDto) {
        Client clientToSave = clientModelMapper.mapRequestDtoToClient(clientRequestDto);
        enrichClient(clientToSave);
        Client createdClient = clientRepository.save(clientToSave);
        newBankAccountLimitUtil.addAccountLimitToTheNewBankAccount(createdClient.getBankAccount());
        return clientModelMapper.mapClientToResponseDto(createdClient);
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
        return clientRepository.findById(id).map(clientModelMapper::mapClientToResponseDto).orElseThrow(()
                -> new NoDataFoundException(String.format(NO_CLIENT_WITH_ID_MSG, id)));
    }

    private void enrichClient(Client client) {
        client.setRegistrationDate(LocalDate.now());
    }
}
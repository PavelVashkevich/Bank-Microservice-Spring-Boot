package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.controller.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.controller.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.model.Client;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {

    ClientResponseDto save(ClientRequestDto clientRequestDto);

    void delete(long id);

    List<Client> findByRegistrationDate(LocalDate registrationDate);

    ClientResponseDto getOne(long id);
}

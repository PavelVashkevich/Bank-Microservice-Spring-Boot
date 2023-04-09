package com.github.pavelvashkevich.bankmicroservice.controller;

import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.exception.BankAccountExistException;
import com.github.pavelvashkevich.bankmicroservice.service.impl.ClientServiceImpl;
import com.github.pavelvashkevich.bankmicroservice.validator.ClientDtoValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/clients")
@AllArgsConstructor
public class ClientController {

    private static final String BANK_ACCOUNT_TAKEN_MSG = "Bank account number has been taken";
    private final ClientServiceImpl clientService;
    private final ClientDtoValidator clientRequestDtoValidator;

    @PostMapping
    public ResponseEntity<ClientResponseDto> newClient(@Valid @RequestBody ClientRequestDto clientRequestDto, BindingResult bindingResult) {

        clientRequestDtoValidator.validate(clientRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BankAccountExistException(BANK_ACCOUNT_TAKEN_MSG);
        }
        ClientResponseDto clientResponseDto = clientService.save(clientRequestDto);
        return ResponseEntity.created(URI.create("/api/v1/clients/" + clientResponseDto.getId())).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable long id) {
        clientService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getClient(@PathVariable long id) {
        ClientResponseDto clientResponseDto = clientService.getClient(id);
        return ResponseEntity.ok(clientResponseDto);
    }
}
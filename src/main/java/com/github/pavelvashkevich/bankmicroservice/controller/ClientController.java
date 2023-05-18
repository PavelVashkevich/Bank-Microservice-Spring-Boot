package com.github.pavelvashkevich.bankmicroservice.controller;

import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.exception.NotValidRequestException;
import com.github.pavelvashkevich.bankmicroservice.service.impl.ClientServiceImpl;
import com.github.pavelvashkevich.bankmicroservice.util.ValidationErrorResponseCreator;
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
    private final ClientServiceImpl clientService;
    private final ValidationErrorResponseCreator validationErrorResponseCreator;

    @PostMapping
    public ResponseEntity<ClientResponseDto> addNewClient(@Valid @RequestBody ClientRequestDto clientRequestDto,
                                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String response = validationErrorResponseCreator.createResponse(bindingResult);
            throw new NotValidRequestException(response);
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
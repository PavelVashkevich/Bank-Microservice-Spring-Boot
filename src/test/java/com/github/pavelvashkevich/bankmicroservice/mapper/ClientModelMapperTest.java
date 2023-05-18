package com.github.pavelvashkevich.bankmicroservice.mapper;

import com.github.pavelvashkevich.bankmicroservice.dto.client.BankAccountClientDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ModelMapper.class, ClientModelMapper.class})
class ClientModelMapperTest {
    private static final long TEST_BANK_ACCOUNT_NUMBER = 7812349871L;
    private static final long TEST_CLIENT_ID = 1L;

    @Autowired
    private ClientModelMapper clientModelMapper;

    @DisplayName("JUnit test for the 'mapRequestDtoToClient' method of the ClientModelMapper class")
    @Test
    public void givenClientRequestDto_whenMapToClient_thenVerifyResult() {
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        BankAccountClientDto bankAccountClientDto = new BankAccountClientDto();
        bankAccountClientDto.setAccountNumber(TEST_BANK_ACCOUNT_NUMBER);
        clientRequestDto.setBankAccount(bankAccountClientDto);

        Client mappedClient = clientModelMapper.mapRequestDtoToClient(clientRequestDto);

        assertThat(mappedClient).isNotNull();
        assertThat(mappedClient.getBankAccount().getAccountNumber()).isEqualTo(TEST_BANK_ACCOUNT_NUMBER);
    }

    @DisplayName("JUnit test for the 'mapClientToResponseDto' method of the ClientModelMapper class")
    @Test
    public void givenClient_whenMapToClientResponseDto_thenVerifyResult() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(TEST_BANK_ACCOUNT_NUMBER);
        Client client = Client.builder().id(TEST_CLIENT_ID).bankAccount(bankAccount).build();

        ClientResponseDto mappedClientToResponseDto = clientModelMapper.mapClientToResponseDto(client);

        assertThat(mappedClientToResponseDto).isNotNull();
        assertThat(mappedClientToResponseDto.getId()).isEqualTo(TEST_CLIENT_ID);
        assertThat(mappedClientToResponseDto.getBankAccount().getAccountNumber()).isEqualTo(TEST_BANK_ACCOUNT_NUMBER);
    }

}
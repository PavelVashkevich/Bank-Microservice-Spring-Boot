package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.dto.client.BankAccountDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.exception.NoDataFoundException;
import com.github.pavelvashkevich.bankmicroservice.mapper.ClientModelMapper;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.Client;
import com.github.pavelvashkevich.bankmicroservice.repository.postgres.ClientRepository;
import com.github.pavelvashkevich.bankmicroservice.service.impl.ClientServiceImpl;
import com.github.pavelvashkevich.bankmicroservice.util.NewBankAccountLimitUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    private static final long TEST_ID = 1L;
    private static final long TEST_BANK_ACCOUNT_NUM = 1234567891L;
    private static final long TEST_BANK_ACCOUNT_2_NUM = 1234567892L;
    private static final int EXPECTED_NUM_OF_DELETE_BY_ID_INV = 1;
    private static final LocalDate TEST_DATE = LocalDate.of(2000, 1, 1);

    private static Client client;
    private static BankAccount bankAccount;

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientModelMapper clientModelMapper;
    @Mock
    private NewBankAccountLimitUtil newBankAccountLimitUtil;
    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    public void setUp() {
        bankAccount = createBankAccount(TEST_BANK_ACCOUNT_NUM);
        client = Client.builder()
                .id(TEST_ID)
                .registrationDate(TEST_DATE)
                .bankAccount(bankAccount)
                .build();
    }

    @DisplayName("Junit Test for save Method of Client Service")
    @Test
    public void givenRequestDtoObject_whenSavedClient_thenReturnClientResponseObject() {
        BankAccountDto bankAccountDto = createBankAccountDto();
        ClientRequestDto clientRequestDto = createClientRequestDto(bankAccountDto);
        ClientResponseDto clientResponseDto = createClientResponseDto(bankAccountDto);

        given(clientModelMapper.mapRequestDtoToClient(clientRequestDto)).willReturn(client);
        given(clientModelMapper.mapClientToResponseDto(client)).willReturn(clientResponseDto);
        given(clientRepository.save(client)).willReturn(client);
        doNothing().when(newBankAccountLimitUtil).addAccountLimitToTheNewBankAccount(bankAccount);

        ClientResponseDto savedResponseDto = clientService.save(clientRequestDto);

        assertThat(savedResponseDto).isNotNull();
    }

    @DisplayName("Junit Test for getClient Method of Client Service")
    @Test
    public void givenClientId_whenGetClient_thenReturnResponseDto() {
        BankAccountDto bankAccountDto = createBankAccountDto();
        ClientResponseDto clientResponseDto = createClientResponseDto(bankAccountDto);

        given(clientRepository.findById(TEST_ID)).willReturn(Optional.of(client));
        given(clientModelMapper.mapClientToResponseDto(client)).willReturn(clientResponseDto);

        ClientResponseDto gotClientResponseDto = clientService.getClient(TEST_ID);
        assertThat(gotClientResponseDto).isNotNull();
    }

    @DisplayName("Junit Test for Exception Thrown in getClient Method of Client Service")
    @Test
    public void givenNotExistingClientId_whenGetClient_thenThrowException() {
        given(clientRepository.findById(TEST_ID)).willReturn(Optional.empty());

        assertThrows(NoDataFoundException.class, () -> clientService.getClient(TEST_ID));
    }

    @DisplayName("Junit Test for findByRegistrationDate Method of Client Service")
    @Test
    public void givenClientsList_whenFindByRegistrationDate_thenReturnClientsList() {
        BankAccount bankAccount2 = createBankAccount(TEST_BANK_ACCOUNT_2_NUM);
        Client client2 = Client.builder()
                .registrationDate(TEST_DATE)
                .bankAccount(bankAccount2)
                .build();

        given(clientRepository.findByRegistrationDate(TEST_DATE)).willReturn(List.of(client, client2));

        List<Client> clients = clientService.findByRegistrationDate(TEST_DATE);

        assertThat(clients).isNotNull();
        assertThat(clients).isNotEmpty();
    }

    @DisplayName("Junit Test for findByRegistrationDate Method of Client Service for empty list result case")
    @Test
    public void givenEmptyClientsList_whenFindByRegistrationDate_thenReturnEmptyClientsList() {
        given(clientRepository.findByRegistrationDate(TEST_DATE)).willReturn(Collections.emptyList());

        List<Client> clients = clientService.findByRegistrationDate(TEST_DATE);

        assertThat(clients).isNotNull();
        assertThat(clients).isEmpty();
    }

    @DisplayName("Junit Test for delete Method of Client Service")
    @Test
    public void givenClientId_whenDeleteClient_thenNothing() {
        given(clientRepository.existsById(TEST_ID)).willReturn(true);
        willDoNothing().given(clientRepository).deleteById(TEST_ID);

        clientService.delete(TEST_ID);

        verify(clientRepository, times(EXPECTED_NUM_OF_DELETE_BY_ID_INV)).deleteById(TEST_ID);
    }

    private ClientRequestDto createClientRequestDto(BankAccountDto bankAccountDto) {
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        bankAccountDto.setAccountNumber(TEST_BANK_ACCOUNT_NUM);
        clientRequestDto.setBankAccount(bankAccountDto);
        return clientRequestDto;
    }

    private BankAccount createBankAccount(long accountNumber) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(accountNumber);
        return bankAccount;
    }

    private BankAccountDto createBankAccountDto() {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setAccountNumber(ClientServiceTest.TEST_BANK_ACCOUNT_NUM);
        return bankAccountDto;
    }

    private ClientResponseDto createClientResponseDto(BankAccountDto bankAccountDto) {
        ClientResponseDto clientResponseDto = new ClientResponseDto();
        clientResponseDto.setId(TEST_ID);
        clientResponseDto.setBankAccount(bankAccountDto);
        return clientResponseDto;
    }
}

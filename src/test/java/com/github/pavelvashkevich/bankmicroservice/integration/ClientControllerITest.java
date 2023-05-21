package com.github.pavelvashkevich.bankmicroservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pavelvashkevich.bankmicroservice.dto.client.BankAccountClientDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientRequestDto;
import com.github.pavelvashkevich.bankmicroservice.dto.client.ClientResponseDto;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.Client;
import com.github.pavelvashkevich.bankmicroservice.repository.postgres.BankAccountRepository;
import com.github.pavelvashkevich.bankmicroservice.repository.postgres.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class ClientControllerITest extends AbstractBaseITest {
    private static final long TEST_BANK_ACCOUNT_NUMBER = 3456123412L;
    private static final long INVALID_BANK_ACCOUNT_NUMBER_MORE_DIGITS = 111222333444L;
    private static final long INVALID_BANK_ACCOUNT_NUMBER_LESS_DIGITS = 123L;
    private static final long NOT_EXIST_CLIENT_ID = 563L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;


    @AfterEach
    public void cleanClientAndBankAccountDatabaseData() {
        clientRepository.deleteAll();
        bankAccountRepository.deleteAll();
    }

    @Test
    public void givenNewValidClient_whenPostMockMvc_thenVerifyResponse() throws Exception {
        String postContent = createClientPostRequestContent(TEST_BANK_ACCOUNT_NUMBER);

        mockMvc.perform(post("/api/v1/clients")
                        .content(postContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/api/v1/clients/*"))
                .andDo(print());
    }

    @Test
    public void givenNewClientWithNullAccountNumber_whenPostMockMvc_thenVerifyResponse() throws Exception {
        String postContent = createClientPostRequestContent(null);

        mockMvc.perform(post("/api/v1/clients")
                        .content(postContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("account_number - must not be null;"))
                .andDo(print());
    }

    @Test
    public void givenNewClientWithExistAccountNumber_whenPostMockMvc_thenVerifyTheResponse() throws Exception {
        Client client = buildClientEntity();
        BankAccount bankAccount = createBankAccount();
        bankAccount.setClient(client);
        client.setBankAccount(bankAccount);
        clientRepository.save(client);

        String postContent = createClientPostRequestContent(bankAccount.getAccountNumber());

        mockMvc.perform(post("/api/v1/clients")
                        .content(postContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("account_number - The given bank account number is already in use;"))
                .andDo(print());
    }

    @Test
    public void givenNewClientWithMoreDigitsInAccountNumber_whenPostMockMvc_thenVerifyResponse() throws Exception {
        String postContent = createClientPostRequestContent(INVALID_BANK_ACCOUNT_NUMBER_MORE_DIGITS);

        mockMvc.perform(post("/api/v1/clients")
                        .content(postContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("account_number - Size of the account number must be 10 digits;"))
                .andDo(print());
    }

    @Test
    public void givenNewClientWithLessDigitsInAccountNumber_whenPostMockMvc_thenVerifyResponse() throws Exception {
        String postContent = createClientPostRequestContent(INVALID_BANK_ACCOUNT_NUMBER_LESS_DIGITS);

        mockMvc.perform(post("/api/v1/clients")
                        .content(postContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("account_number - Size of the account number must be 10 digits;"))
                .andDo(print());
    }

    @Test
    public void givenNewClientWithNullBankAccount_whenPostMockMvc_thenVerifyResponse() throws Exception {
        ClientRequestDto clientRequestDto = createClientRequestDto(null);
        String postContent = objectMapper.writeValueAsString(clientRequestDto);

        mockMvc.perform(post("/api/v1/clients")
                        .content(postContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("bank_account - must not be null;"))
                .andDo(print());
    }

    @Test
    public void givenExistClient_whenDeleteMockMvc_thenVerifyResponse() throws Exception {
        Client client = buildClientEntity();
        clientRepository.save(client);

        mockMvc.perform(delete("/api/v1/clients/delete/" + client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"OK\""))
                .andDo(print());
    }

    @Test
    public void givenNotExistClient_whenDeleteMockMvc_thenVerifyResponse() throws Exception {
        mockMvc.perform(delete(String.format("/api/v1/clients/delete/%d", NOT_EXIST_CLIENT_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"OK\""))
                .andDo(print());
    }

    @Test
    public void givenExistClientId_whenGetMockMvc_thenVerifyTheResponse() throws Exception {
        Client client = buildClientEntity();
        BankAccount bankAccount = createBankAccount();
        bankAccount.setClient(client);
        client.setBankAccount(bankAccount);
        Client savedClient = clientRepository.save(client);

        String expectedContent = createClientResponseExpectedContent(savedClient.getId(),
                savedClient.getBankAccount().getAccountNumber());

        mockMvc.perform(get("/api/v1/clients/" + savedClient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedContent))
                .andDo(print());
    }

    private Client buildClientEntity() {
        return Client.builder().registrationDate(LocalDate.now()).build();
    }

    private BankAccount createBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(TEST_BANK_ACCOUNT_NUMBER);
        return bankAccount;
    }

    private BankAccountClientDto createBankAccountDto(Long accountNumber) {
        BankAccountClientDto bankAccountDto = new BankAccountClientDto();
        bankAccountDto.setAccountNumber(accountNumber);
        return bankAccountDto;
    }

    private ClientRequestDto createClientRequestDto(BankAccountClientDto bankAccountDto) {
        ClientRequestDto clientRequestDto = new ClientRequestDto();
        clientRequestDto.setBankAccount(bankAccountDto);
        return clientRequestDto;
    }

    private ClientResponseDto createClientResponseDto(Long clientId, BankAccountClientDto bankAccountDto) {
        ClientResponseDto clientResponseDto = new ClientResponseDto();
        clientResponseDto.setId(clientId);
        clientResponseDto.setBankAccount(bankAccountDto);
        return clientResponseDto;
    }

    private String createClientPostRequestContent(Long accountNumber) throws JsonProcessingException {
        BankAccountClientDto bankAccountDto = createBankAccountDto(accountNumber);
        ClientRequestDto clientRequestDto = createClientRequestDto(bankAccountDto);
        return objectMapper.writeValueAsString(clientRequestDto);
    }

    private String createClientResponseExpectedContent(Long clientId, Long accountNumber) throws JsonProcessingException {
        BankAccountClientDto bankAccountDto = createBankAccountDto(accountNumber);
        ClientResponseDto clientResponseDto = createClientResponseDto(clientId, bankAccountDto);
        return objectMapper.writeValueAsString(clientResponseDto);
    }
}

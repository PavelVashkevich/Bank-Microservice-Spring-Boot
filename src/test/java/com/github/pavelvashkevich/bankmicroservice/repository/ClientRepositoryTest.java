package com.github.pavelvashkevich.bankmicroservice.repository;


import com.github.pavelvashkevich.bankmicroservice.model.postgres.BankAccount;
import com.github.pavelvashkevich.bankmicroservice.model.postgres.Client;
import com.github.pavelvashkevich.bankmicroservice.repository.postgres.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClientRepositoryTest {
    private static final long TEST_BANK_ACCOUNT_1_NUM = 1234567891L;
    private static final long TEST_BANK_ACCOUNT_2_NUM = 1234567892L;
    private static final LocalDate TEST_DATE = LocalDate.of(2000, 1, 1);

    private static Client client;

    private final ClientRepository clientRepository;

    @Autowired
    public ClientRepositoryTest(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @BeforeEach
    public void setUp() {
        BankAccount bankAccount = new BankAccount();
        client = Client.builder()
                .registrationDate(TEST_DATE)
                .bankAccount(bankAccount)
                .build();
        bankAccount.setAccountNumber(TEST_BANK_ACCOUNT_1_NUM);
        bankAccount.setClient(client);
    }

    @DisplayName("Junit Test for save operation of Client Repository")
    @Test
    public void givenClientObject_whenSave_thenReturnSavedClient() {
        Client savedClient = clientRepository.save(client);

        assertThat(client.getId()).isNotNull();
        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getBankAccount().getAccountNumber()).isEqualTo(TEST_BANK_ACCOUNT_1_NUM);
    }

    @DisplayName("Junit Test for findById operation of Client Repository")
    @Test
    public void givenClientObject_whenFindById_thenReturnClient() {
        clientRepository.save(client);
        Client savedClient = clientRepository.findById(client.getId()).orElse(null);

        assertThat(client.getId()).isNotNull();
        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getBankAccount().getAccountNumber()).isEqualTo(TEST_BANK_ACCOUNT_1_NUM);
    }

    @DisplayName("JUnit test for findByRegistrationDate operation of Client Repository")
    @Test
    public void givenClientsList_whenFindByRegistrationDate_thenClientsList() {
        BankAccount bankAccount2 = new BankAccount();
        Client client2 = Client.builder()
                .registrationDate(TEST_DATE)
                .bankAccount(bankAccount2)
                .build();
        bankAccount2.setAccountNumber(TEST_BANK_ACCOUNT_2_NUM);
        bankAccount2.setClient(client2);
        List<Client> clients = List.of(client, client2);

        clientRepository.saveAll(clients);
        List<Client> foundClients = clientRepository.findByRegistrationDate(TEST_DATE);

        assertThat(foundClients).isNotNull();
        assertThat(foundClients.size()).isEqualTo(clients.size());
    }

    @DisplayName("JUnit test for delete operation of Client Repository")
    @Test
    public void givenClientId_whenDelete_thenRemoveClient() {
        clientRepository.save(client);

        clientRepository.deleteById(client.getId());
        Optional<Client> clientOptional = clientRepository.findById(client.getId());

        assertThat(clientOptional).isEmpty();
    }
}
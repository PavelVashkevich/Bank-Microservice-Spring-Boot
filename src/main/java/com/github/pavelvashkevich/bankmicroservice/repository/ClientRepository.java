package com.github.pavelvashkevich.bankmicroservice.repository;

import com.github.pavelvashkevich.bankmicroservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByRegistrationDate(LocalDate registrationDate);
}

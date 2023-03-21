package com.github.pavelvashkevich.bankmicroservice.repository;


import com.github.pavelvashkevich.bankmicroservice.model.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {

    Optional<CurrencyExchange> findBySymbolAndDatetime(String symbol, LocalDate datetime);

    @Query(value = "SELECT * FROM currency_exchanges WHERE symbol = ?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<CurrencyExchange> findLatestBySymbol(String symbol);
}
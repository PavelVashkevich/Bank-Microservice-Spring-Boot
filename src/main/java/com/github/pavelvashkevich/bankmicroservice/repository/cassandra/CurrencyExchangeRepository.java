package com.github.pavelvashkevich.bankmicroservice.repository.cassandra;

import com.github.pavelvashkevich.bankmicroservice.model.cassandra.CurrencyExchange;
import com.github.pavelvashkevich.bankmicroservice.model.cassandra.CurrencyExchangeKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface CurrencyExchangeRepository extends CassandraRepository<CurrencyExchange, CurrencyExchangeKey> {

}
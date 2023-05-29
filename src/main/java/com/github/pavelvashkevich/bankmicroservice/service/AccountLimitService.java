package com.github.pavelvashkevich.bankmicroservice.service;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;

import java.time.LocalDate;
import java.util.List;

public interface AccountLimitService {

    void save(AccountLimit limit);

    void update(AccountLimit limit);

    List<AccountLimit> findByDate(LocalDate date);

    void saveAll(List<AccountLimit> accountLimitForExpenseCategories);

    void flush();
}

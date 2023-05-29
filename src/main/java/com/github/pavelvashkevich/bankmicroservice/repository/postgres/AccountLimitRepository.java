package com.github.pavelvashkevich.bankmicroservice.repository.postgres;

import com.github.pavelvashkevich.bankmicroservice.model.postgres.AccountLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountLimitRepository extends JpaRepository<AccountLimit, Long> {

    @Query(value = "SELECT * FROM account_limit WHERE date(datetime) = ?1", nativeQuery = true)
    List<AccountLimit> findByDate(LocalDate date);

}

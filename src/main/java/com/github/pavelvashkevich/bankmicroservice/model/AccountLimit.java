package com.github.pavelvashkevich.bankmicroservice.model;

import com.github.pavelvashkevich.bankmicroservice.types.Currency;
import com.github.pavelvashkevich.bankmicroservice.types.ExpenseCategory;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class AccountLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Currency currencyShortname;
    @Enumerated(value = EnumType.STRING)
    private ExpenseCategory expenseCategory;
    @PositiveOrZero(message = "Sum must be equal of higher than 0")
    @NotNull
    private BigDecimal sum;
    @PastOrPresent(message = "Datetime cannot be set to the future")
    @NotNull
    private ZonedDateTime datetime;
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private BankAccount bankAccount;
    @OneToMany(mappedBy = "accountLimit")
    private List<Transaction> transactions;
}

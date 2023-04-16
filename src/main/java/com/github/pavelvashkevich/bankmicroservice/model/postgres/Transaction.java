package com.github.pavelvashkevich.bankmicroservice.model.postgres;

import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.ExpenseCategory;
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
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Digits(integer = 10, fraction = 0, message = "'accountFrom' must contain 10 digits")
    @NotNull
    private Integer accountFrom;
    @Digits(integer = 10, fraction = 0, message = "'accountTo' must contain 10 digits")
    @NotNull
    private Integer accountTo;
    @Pattern(regexp = "[A-Z]{3}")
    private String currencyShortname;
    @PositiveOrZero(message = "Sum must be equal of higher than 0")
    @NotNull
    private BigDecimal sum;
    @Enumerated(value = EnumType.STRING)
    private ExpenseCategory expenseCategory;
    @PastOrPresent(message = "Datetime cannot be set to the future")
    @NotNull
    private ZonedDateTime datetime;
    @NotNull
    private Boolean limitExceeded;
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private BankAccount bankAccount;
    @ManyToOne
    @JoinColumn(name = "account_limit_id", referencedColumnName = "id")
    private AccountLimit accountLimit;
}

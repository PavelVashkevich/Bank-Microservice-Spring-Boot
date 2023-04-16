package com.github.pavelvashkevich.bankmicroservice.model.postgres;

import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.Currency;
import com.github.pavelvashkevich.bankmicroservice.model.types.enumerators.ExpenseCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccountLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator_account_limit")
    @SequenceGenerator(name = "seq_generator_account_limit", sequenceName = "account_limit_id_seq", allocationSize = 1)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Currency currencyShortname;
    @Enumerated(value = EnumType.STRING)
    @ColumnTransformer(read = "UPPER(expense_category)", write = "LOWER(?)")
    private ExpenseCategory expenseCategory;
    @PositiveOrZero(message = "Sum must be equal of higher than 0")
    @NotNull
    private BigDecimal sum;
    @NotNull
    private BigDecimal remainingSum;
    @PastOrPresent(message = "Datetime cannot be set to the future")
    @NotNull
    private ZonedDateTime datetime;
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private BankAccount bankAccount;
    @OneToMany(mappedBy = "accountLimit")
    private List<Transaction> transactions;
}

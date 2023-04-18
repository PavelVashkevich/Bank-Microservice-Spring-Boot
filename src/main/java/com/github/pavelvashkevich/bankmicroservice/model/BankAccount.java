package com.github.pavelvashkevich.bankmicroservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Digits(integer = 10, fraction = 0, message = "'account_number' must contain 10 digits")
    private Integer accountNumber;
    @OneToOne
    @JoinColumn(name = "client_id",referencedColumnName = "id")
    private Client client;
    @OneToMany(mappedBy = "bankAccount")
    private List<AccountLimit> accountLimits;
    @OneToMany(mappedBy = "bankAccount")
    private List<Transaction> transactions;
}
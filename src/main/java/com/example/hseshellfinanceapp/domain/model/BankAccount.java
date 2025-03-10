package com.example.hseshellfinanceapp.domain.model;


import java.math.BigDecimal;
import java.util.UUID;

public class BankAccount {
    private final UUID id;
    private String name;
    private BigDecimal balance;

    public BankAccount(UUID id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public void updateBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public boolean hasSufficientFunds(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    @Override
    public String toString() {
        return String.format("Bank account: %s  | Balance: %.2f",
                name, balance);
    }

    public String toDetailedString() {
        return String.format("""
                        ID: %s
                        Name: %s
                        Balance: %.2f""",
                id, name, balance);
    }
}

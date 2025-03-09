package com.example.hseshellfinanceapp.domain.model;


import java.math.BigDecimal;
import java.util.UUID;

public class BankAccount {
    private final UUID id;
    private String name;
    private BigDecimal balance;
    private String passwordHash;
    private String phoneNumber;
    private String cardNumber;

    public BankAccount(UUID id, String name, BigDecimal balance,
                       String passwordHash, String phoneNumber, String cardNumber) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.cardNumber = cardNumber;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    public void updateBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public boolean hasSufficientFunds(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    @Override
    public String toString() {
        return String.format("Bank account: %s | Card number: %s | Balance: %.2f",
                name, getMaskedCardNumber(), balance);
    }

    public String toDetailedString() {
        return String.format("""
                        ID: %s
                        Name: %s
                        Card number: %s
                        Phone: %s
                        Balance: %.2f""",
                id, name, getMaskedCardNumber(), phoneNumber, balance);
    }
}

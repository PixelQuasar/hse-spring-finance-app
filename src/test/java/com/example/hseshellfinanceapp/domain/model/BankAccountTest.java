package com.example.hseshellfinanceapp.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankAccountTest {

    @Test
    void constructor_shouldInitializeAllFields() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Test Account";
        BigDecimal balance = new BigDecimal("100.00");

        // When
        BankAccount account = new BankAccount(id, name, balance);

        // Then
        assertEquals(id, account.getId());
        assertEquals(name, account.getName());
        assertEquals(balance, account.getBalance());
    }

    @Test
    void getName_shouldReturnCorrectName() {
        // Given
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test Account", BigDecimal.ZERO);

        // When
        String result = account.getName();

        // Then
        assertEquals("Test Account", result);
    }

    @Test
    void setName_shouldUpdateName() {
        // Given
        BankAccount account = new BankAccount(UUID.randomUUID(), "Old Name", BigDecimal.ZERO);

        // When
        account.setName("New Name");

        // Then
        assertEquals("New Name", account.getName());
    }

    @Test
    void getBalance_shouldReturnCorrectBalance() {
        // Given
        BigDecimal balance = new BigDecimal("150.75");
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test Account", balance);

        // When
        BigDecimal result = account.getBalance();

        // Then
        assertEquals(balance, result);
    }

    @Test
    void setBalance_shouldUpdateBalance() {
        // Given
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test Account", BigDecimal.ZERO);
        BigDecimal newBalance = new BigDecimal("200.50");

        // When
        account.setBalance(newBalance);

        // Then
        assertEquals(newBalance, account.getBalance());
    }

    @Test
    void updateBalance_withPositiveAmount_shouldIncreaseBalance() {
        // Given
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test Account", new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("50.00");

        // When
        account.updateBalance(amount);

        // Then
        assertEquals(new BigDecimal("150.00"), account.getBalance());
    }

    @Test
    void updateBalance_withNegativeAmount_shouldDecreaseBalance() {
        // Given
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test Account", new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("-30.00");

        // When
        account.updateBalance(amount);

        // Then
        assertEquals(new BigDecimal("70.00"), account.getBalance());
    }

    @Test
    void hasSufficientFunds_whenBalanceGreaterThanAmount_shouldReturnTrue() {
        // Given
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test Account", new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("50.00");

        // When
        boolean result = account.hasSufficientFunds(amount);

        // Then
        assertTrue(result);
    }

    @Test
    void hasSufficientFunds_whenBalanceEqualToAmount_shouldReturnTrue() {
        // Given
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test Account", new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("100.00");

        // When
        boolean result = account.hasSufficientFunds(amount);

        // Then
        assertTrue(result);
    }

    @Test
    void hasSufficientFunds_whenBalanceLessThanAmount_shouldReturnFalse() {
        // Given
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test Account", new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("150.00");

        // When
        boolean result = account.hasSufficientFunds(amount);

        // Then
        assertFalse(result);
    }

    @Test
    void toString_shouldReturnFormattedString() {
        // Given
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test Account", new BigDecimal("100.50"));

        // When
        String result = account.toString();

        // Then
        assertEquals("Bank account: Test Account  | Balance: 100.50", result);
    }

    @Test
    void toDetailedString_shouldReturnFormattedString() {
        // Given
        UUID id = UUID.randomUUID();
        BankAccount account = new BankAccount(id, "Test Account", new BigDecimal("100.50"));

        // When
        String result = account.toDetailedString();

        // Then
        String expected = String.format("""
                ID: %s
                Name: Test Account
                Balance: 100.50""", id);
        assertEquals(expected, result);
    }
}

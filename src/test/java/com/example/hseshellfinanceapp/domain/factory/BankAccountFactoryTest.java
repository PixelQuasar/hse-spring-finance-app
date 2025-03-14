package com.example.hseshellfinanceapp.domain.factory;

import java.math.BigDecimal;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankAccountFactoryTest {

    private BankAccountFactory bankAccountFactory;

    @BeforeEach
    void setUp() {
        bankAccountFactory = new BankAccountFactory();
    }

    @Test
    void createAccount_WithValidInput_ShouldCreateAccount() {
        // Given
        String name = "Test Account";
        BigDecimal initialBalance = new BigDecimal("100.00");

        // When
        BankAccount account = bankAccountFactory.createAccount(name, initialBalance);

        // Then
        assertNotNull(account);
        assertNotNull(account.getId());
        assertEquals(name, account.getName());
        assertEquals(initialBalance, account.getBalance());
    }

    @Test
    void createBasicAccount_WithValidName_ShouldCreateAccountWithZeroBalance() {
        // Given
        String name = "Basic Account";

        // When
        BankAccount account = bankAccountFactory.createBasicAccount(name);

        // Then
        assertNotNull(account);
        assertNotNull(account.getId());
        assertEquals(name, account.getName());
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void createAccount_WithNullName_ShouldThrowException() {
        // Given
        String name = null;
        BigDecimal initialBalance = BigDecimal.TEN;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bankAccountFactory.createAccount(name, initialBalance)
        );
        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void createAccount_WithEmptyName_ShouldThrowException() {
        // Given
        String name = "   ";
        BigDecimal initialBalance = BigDecimal.TEN;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bankAccountFactory.createAccount(name, initialBalance)
        );
        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void createAccount_WithTooLongName_ShouldThrowException() {
        // Given
        String name = "a".repeat(256);  // 256 characters
        BigDecimal initialBalance = BigDecimal.TEN;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bankAccountFactory.createAccount(name, initialBalance)
        );
        assertEquals("Name is too long", exception.getMessage());
    }

    @Test
    void createAccount_WithNullBalance_ShouldThrowException() {
        // Given
        String name = "Test Account";
        BigDecimal initialBalance = null;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bankAccountFactory.createAccount(name, initialBalance)
        );
        assertEquals("The balance cannot be null", exception.getMessage());
    }

    @Test
    void createAccount_WithNegativeBalance_ShouldThrowException() {
        // Given
        String name = "Test Account";
        BigDecimal initialBalance = new BigDecimal("-1.00");

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bankAccountFactory.createAccount(name, initialBalance)
        );
        assertEquals("Start balance cannot be negative", exception.getMessage());
    }

    @Test
    void createBasicAccount_WithNullName_ShouldThrowException() {
        // Given
        String name = null;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bankAccountFactory.createBasicAccount(name)
        );
        assertEquals("Name cannot be empty", exception.getMessage());
    }
}

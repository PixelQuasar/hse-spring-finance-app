package com.example.hseshellfinanceapp.domain.factory;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankAccountFactoryTest {

    private BankAccountFactory factory;

    @BeforeEach
    void setUp() {
        factory = new BankAccountFactory();
    }

    @Test
    void createAccount_withValidData_shouldCreateAccount() {
        // Given
        String name = "Test Account";
        BigDecimal balance = new BigDecimal("100.00");

        // When
        BankAccount account = factory.createAccount(name, balance);

        // Then
        assertNotNull(account);
        assertNotNull(account.getId());
        assertEquals(name, account.getName());
        assertEquals(balance, account.getBalance());
    }

    @Test
    void createBasicAccount_shouldCreateAccountWithZeroBalance() {
        // Given
        String name = "Basic Account";

        // When
        BankAccount account = factory.createBasicAccount(name);

        // Then
        assertNotNull(account);
        assertEquals(name, account.getName());
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void createAccount_withNullName_shouldThrowException() {
        // Given
        String name = null;
        BigDecimal balance = BigDecimal.TEN;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createAccount(name, balance)
        );
        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void createAccount_withEmptyName_shouldThrowException() {
        // Given
        String name = "   ";
        BigDecimal balance = BigDecimal.TEN;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createAccount(name, balance)
        );
        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void createAccount_withTooLongName_shouldThrowException() {
        // Given
        String name = "a".repeat(256); // 256 characters
        BigDecimal balance = BigDecimal.TEN;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createAccount(name, balance)
        );
        assertEquals("Name is too long", exception.getMessage());
    }

    @Test
    void createAccount_withNullBalance_shouldThrowException() {
        // Given
        String name = "Test Account";
        BigDecimal balance = null;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createAccount(name, balance)
        );
        assertEquals("The balance cannot be null", exception.getMessage());
    }

    @Test
    void createAccount_withNegativeBalance_shouldThrowException() {
        // Given
        String name = "Test Account";
        BigDecimal balance = new BigDecimal("-10.00");

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createAccount(name, balance)
        );
        assertEquals("Start balance cannot be negative", exception.getMessage());
    }

    // Testing private validation methods using reflection
    @Test
    void validatePassword_withNullPassword_shouldThrowException() throws Exception {
        // Given
        String password = null;
        Method validatePasswordMethod = BankAccountFactory.class.getDeclaredMethod("validatePassword", String.class);
        validatePasswordMethod.setAccessible(true);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validatePasswordMethod.invoke(factory, password)
        );
        assertTrue(exception.getCause().getMessage().contains("Password cannot be empty"));
    }

    @Test
    void validatePassword_withShortPassword_shouldThrowException() throws Exception {
        // Given
        String password = "12345"; // 5 characters
        Method validatePasswordMethod = BankAccountFactory.class.getDeclaredMethod("validatePassword", String.class);
        validatePasswordMethod.setAccessible(true);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validatePasswordMethod.invoke(factory, password)
        );
        assertTrue(exception.getCause().getMessage().contains("Password should be at least 6 characters long"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"+1234567890", "+123456789012345"})
    void validatePhoneNumber_withValidPhoneNumber_shouldNotThrowException(String phoneNumber) throws Exception {
        // Given
        Method validatePhoneNumberMethod = BankAccountFactory.class.getDeclaredMethod("validatePhoneNumber",
                String.class);
        validatePhoneNumberMethod.setAccessible(true);

        // When/Then
        assertDoesNotThrow(() -> validatePhoneNumberMethod.invoke(factory, phoneNumber));
    }

    @Test
    void validatePhoneNumber_withInvalidPhoneNumber_shouldThrowException() throws Exception {
        // Given
        String phoneNumber = "abc123";
        Method validatePhoneNumberMethod = BankAccountFactory.class.getDeclaredMethod("validatePhoneNumber",
                String.class);
        validatePhoneNumberMethod.setAccessible(true);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validatePhoneNumberMethod.invoke(factory, phoneNumber)
        );
        assertTrue(exception.getCause().getMessage().contains("Invalid phone number format"));
    }

    @Test
    void validateCardNumber_withValidCardNumber_shouldNotThrowException() throws Exception {
        // Given
        String cardNumber = "1234567890123456"; // 16 digits
        Method validateCardNumberMethod = BankAccountFactory.class.getDeclaredMethod("validateCardNumber",
                String.class);
        validateCardNumberMethod.setAccessible(true);

        // When/Then
        assertDoesNotThrow(() -> validateCardNumberMethod.invoke(factory, cardNumber));
    }

    @Test
    void validateCardNumber_withInvalidCardNumber_shouldThrowException() throws Exception {
        // Given
        String cardNumber = "12345"; // Not 16 digits
        Method validateCardNumberMethod = BankAccountFactory.class.getDeclaredMethod("validateCardNumber",
                String.class);
        validateCardNumberMethod.setAccessible(true);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validateCardNumberMethod.invoke(factory, cardNumber)
        );
        assertTrue(exception.getCause().getMessage().contains("Card number should be 16 digits"));
    }
}

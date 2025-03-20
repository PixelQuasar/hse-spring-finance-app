package com.example.hseshellfinanceapp.domain.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperationFactoryTest {

    private OperationFactory factory;
    private UUID accountId;
    private UUID categoryId;
    private LocalDateTime date;

    @BeforeEach
    void setUp() {
        factory = new OperationFactory();
        accountId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        date = LocalDateTime.now();
    }

    @Test
    void createOperation_withValidData_shouldCreateOperation() {
        // Given
        OperationType type = OperationType.EXPENSE;
        BigDecimal amount = new BigDecimal("50.00");
        String description = "Grocery shopping";

        // When
        Operation operation = factory.createOperation(
                type, accountId, amount, date, description, categoryId);

        // Then
        assertNotNull(operation);
        assertNotNull(operation.getId());
        assertEquals(type, operation.getType());
        assertEquals(accountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(date, operation.getDate());
        assertEquals(description, operation.getDescription());
        assertEquals(categoryId, operation.getCategoryId());
    }

    @Test
    void createIncome_shouldCreateIncomeOperation() {
        // Given
        BigDecimal amount = new BigDecimal("100.00");
        String description = "Salary";

        // When
        Operation operation = factory.createIncome(
                accountId, amount, date, description, categoryId);

        // Then
        assertNotNull(operation);
        assertEquals(OperationType.INCOME, operation.getType());
        assertEquals(accountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(date, operation.getDate());
        assertEquals(description, operation.getDescription());
        assertEquals(categoryId, operation.getCategoryId());
    }

    @Test
    void createExpense_shouldCreateExpenseOperation() {
        // Given
        BigDecimal amount = new BigDecimal("75.50");
        String description = "Restaurant bill";

        // When
        Operation operation = factory.createExpense(
                accountId, amount, date, description, categoryId);

        // Then
        assertNotNull(operation);
        assertEquals(OperationType.EXPENSE, operation.getType());
        assertEquals(accountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(date, operation.getDate());
        assertEquals(description, operation.getDescription());
        assertEquals(categoryId, operation.getCategoryId());
    }

    @Test
    void createOperation_withNullType_shouldThrowException() {
        // Given
        OperationType type = null;
        BigDecimal amount = new BigDecimal("50.00");

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createOperation(type, accountId, amount, date, "Test", categoryId)
        );
        assertEquals("Operation type cannot be null", exception.getMessage());
    }

    @Test
    void createOperation_withNullBankAccountId_shouldThrowException() {
        // Given
        UUID nullAccountId = null;
        BigDecimal amount = new BigDecimal("50.00");

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createOperation(OperationType.EXPENSE, nullAccountId, amount, date, "Test", categoryId)
        );
        assertEquals("Bank account ID cannot be null", exception.getMessage());
    }

    @Test
    void createOperation_withNullAmount_shouldThrowException() {
        // Given
        BigDecimal nullAmount = null;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createOperation(OperationType.EXPENSE, accountId, nullAmount, date, "Test", categoryId)
        );
        assertEquals("Amount cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.00", "-10.00"})
    void createOperation_withZeroOrNegativeAmount_shouldThrowException(String amountStr) {
        // Given
        BigDecimal amount = new BigDecimal(amountStr);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createOperation(OperationType.EXPENSE, accountId, amount, date, "Test", categoryId)
        );
        assertEquals("Amount must be greater than zero", exception.getMessage());
    }

    @Test
    void createOperation_withNullDate_shouldThrowException() {
        // Given
        LocalDateTime nullDate = null;
        BigDecimal amount = new BigDecimal("50.00");

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createOperation(OperationType.EXPENSE, accountId, amount, nullDate, "Test", categoryId)
        );
        assertEquals("Date cannot be null", exception.getMessage());
    }

    @Test
    void createOperation_withNullCategoryId_shouldThrowException() {
        // Given
        UUID nullCategoryId = null;
        BigDecimal amount = new BigDecimal("50.00");

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createOperation(OperationType.EXPENSE, accountId, amount, date, "Test", nullCategoryId)
        );
        assertEquals("Category ID cannot be null", exception.getMessage());
    }
}

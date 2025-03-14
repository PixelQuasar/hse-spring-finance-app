package com.example.hseshellfinanceapp.domain.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperationFactoryTest {

    private OperationFactory operationFactory;
    private UUID bankAccountId;
    private UUID categoryId;
    private LocalDateTime date;

    @BeforeEach
    void setUp() {
        operationFactory = new OperationFactory();
        bankAccountId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        date = LocalDateTime.now();
    }

    @Test
    void createOperation_WithValidInput_ShouldCreateOperation() {
        // Given
        OperationType type = OperationType.INCOME;
        BigDecimal amount = new BigDecimal("50.00");
        String description = "Test operation";

        // When
        Operation operation = operationFactory.createOperation(type, bankAccountId, amount, date, description,
                categoryId);

        // Then
        assertNotNull(operation);
        assertNotNull(operation.getId());
        assertEquals(type, operation.getType());
        assertEquals(bankAccountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(date, operation.getDate());
        assertEquals(description, operation.getDescription());
        assertEquals(categoryId, operation.getCategoryId());
    }

    @Test
    void createIncome_WithValidInput_ShouldCreateIncomeOperation() {
        // Given
        BigDecimal amount = new BigDecimal("100.00");
        String description = "Salary";

        // When
        Operation operation = operationFactory.createIncome(bankAccountId, amount, date, description, categoryId);

        // Then
        assertNotNull(operation);
        assertNotNull(operation.getId());
        assertEquals(OperationType.INCOME, operation.getType());
        assertEquals(bankAccountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(date, operation.getDate());
        assertEquals(description, operation.getDescription());
        assertEquals(categoryId, operation.getCategoryId());
    }

    @Test
    void createExpense_WithValidInput_ShouldCreateExpenseOperation() {
        // Given
        BigDecimal amount = new BigDecimal("30.00");
        String description = "Groceries";

        // When
        Operation operation = operationFactory.createExpense(bankAccountId, amount, date, description, categoryId);

        // Then
        assertNotNull(operation);
        assertNotNull(operation.getId());
        assertEquals(OperationType.EXPENSE, operation.getType());
        assertEquals(bankAccountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(date, operation.getDate());
        assertEquals(description, operation.getDescription());
        assertEquals(categoryId, operation.getCategoryId());
    }

    @Test
    void createOperation_WithoutDate_ShouldCreateOperationWithCurrentDate() {
        // Given
        OperationType type = OperationType.EXPENSE;
        BigDecimal amount = new BigDecimal("75.00");
        String description = "Shopping";

        // When
        Operation operation = operationFactory.createOperation(type, bankAccountId, amount, LocalDateTime.now(),
                description, categoryId);

        // Then
        assertNotNull(operation);
        assertNotNull(operation.getDate());
        assertEquals(type, operation.getType());
        assertEquals(bankAccountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(description, operation.getDescription());
        assertEquals(categoryId, operation.getCategoryId());
    }

    @Test
    void createOperation_WithNullType_ShouldThrowException() {
        // Given
        OperationType type = null;
        BigDecimal amount = new BigDecimal("50.00");
        String description = "Test operation";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operationFactory.createOperation(type, bankAccountId, amount, date, description, categoryId)
        );
        assertEquals("Operation type cannot be null", exception.getMessage());
    }

    @Test
    void createOperation_WithNullBankAccountId_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        UUID nullBankAccountId = null;
        BigDecimal amount = new BigDecimal("50.00");
        String description = "Test operation";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operationFactory.createOperation(type, nullBankAccountId, amount, date, description, categoryId)
        );
        assertEquals("Bank account ID cannot be null", exception.getMessage());
    }

    @Test
    void createOperation_WithNullAmount_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        BigDecimal amount = null;
        String description = "Test operation";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operationFactory.createOperation(type, bankAccountId, amount, date, description, categoryId)
        );
        assertEquals("Amount cannot be null", exception.getMessage());
    }

    @Test
    void createOperation_WithZeroAmount_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        BigDecimal amount = BigDecimal.ZERO;
        String description = "Test operation";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operationFactory.createOperation(type, bankAccountId, amount, date, description, categoryId)
        );
        assertEquals("Amount must be greater than zero", exception.getMessage());
    }

    @Test
    void createOperation_WithNegativeAmount_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        BigDecimal amount = new BigDecimal("-10.00");
        String description = "Test operation";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operationFactory.createOperation(type, bankAccountId, amount, date, description, categoryId)
        );
        assertEquals("Amount must be greater than zero", exception.getMessage());
    }

    @Test
    void createOperation_WithNullDate_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        BigDecimal amount = new BigDecimal("50.00");
        LocalDateTime nullDate = null;
        String description = "Test operation";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operationFactory.createOperation(type, bankAccountId, amount, nullDate, description, categoryId)
        );
        assertEquals("Date cannot be null", exception.getMessage());
    }

    @Test
    void createOperation_WithNullCategoryId_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        BigDecimal amount = new BigDecimal("50.00");
        String description = "Test operation";
        UUID nullCategoryId = null;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operationFactory.createOperation(type, bankAccountId, amount, date, description, nullCategoryId)
        );
        assertEquals("Category ID cannot be null", exception.getMessage());
    }

    @Test
    void createIncome_WithNullBankAccountId_ShouldThrowException() {
        // Given
        UUID nullBankAccountId = null;
        BigDecimal amount = new BigDecimal("50.00");
        String description = "Test income";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operationFactory.createIncome(nullBankAccountId, amount, date, description, categoryId)
        );
        assertEquals("Bank account ID cannot be null", exception.getMessage());
    }

    @Test
    void createExpense_WithNullBankAccountId_ShouldThrowException() {
        // Given
        UUID nullBankAccountId = null;
        BigDecimal amount = new BigDecimal("50.00");
        String description = "Test expense";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operationFactory.createExpense(nullBankAccountId, amount, date, description, categoryId)
        );
        assertEquals("Bank account ID cannot be null", exception.getMessage());
    }
}

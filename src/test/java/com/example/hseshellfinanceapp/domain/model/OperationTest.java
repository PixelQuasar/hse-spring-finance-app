package com.example.hseshellfinanceapp.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationTest {

    @Test
    void constructor_shouldInitializeAllFields() {
        // Given
        UUID id = UUID.randomUUID();
        OperationType type = OperationType.EXPENSE;
        UUID bankAccountId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("75.50");
        LocalDateTime date = LocalDateTime.now();
        String description = "Grocery shopping";
        UUID categoryId = UUID.randomUUID();

        // When
        Operation operation = new Operation(id, type, bankAccountId, amount, date, description, categoryId);

        // Then
        assertEquals(id, operation.getId());
        assertEquals(type, operation.getType());
        assertEquals(bankAccountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(date, operation.getDate());
        assertEquals(description, operation.getDescription());
        assertEquals(categoryId, operation.getCategoryId());
    }

    @Test
    void getType_shouldReturnCorrectType() {
        // Given
        OperationType type = OperationType.EXPENSE;
        Operation operation = createTestOperation(type);

        // When
        OperationType result = operation.getType();

        // Then
        assertEquals(type, result);
    }

    @Test
    void setType_shouldUpdateType() {
        // Given
        Operation operation = createTestOperation(OperationType.EXPENSE);

        // When
        operation.setType(OperationType.INCOME);

        // Then
        assertEquals(OperationType.INCOME, operation.getType());
    }

    @Test
    void getBankAccountId_shouldReturnCorrectId() {
        // Given
        UUID bankAccountId = UUID.randomUUID();
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.EXPENSE, bankAccountId,
                BigDecimal.TEN, LocalDateTime.now(), "Test", UUID.randomUUID()
        );

        // When
        UUID result = operation.getBankAccountId();

        // Then
        assertEquals(bankAccountId, result);
    }

    @Test
    void setBankAccountId_shouldUpdateBankAccountId() {
        // Given
        Operation operation = createTestOperation(OperationType.EXPENSE);
        UUID newBankAccountId = UUID.randomUUID();

        // When
        operation.setBankAccountId(newBankAccountId);

        // Then
        assertEquals(newBankAccountId, operation.getBankAccountId());
    }

    @Test
    void getAmount_shouldReturnCorrectAmount() {
        // Given
        BigDecimal amount = new BigDecimal("123.45");
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.EXPENSE, UUID.randomUUID(),
                amount, LocalDateTime.now(), "Test", UUID.randomUUID()
        );

        // When
        BigDecimal result = operation.getAmount();

        // Then
        assertEquals(amount, result);
    }

    @Test
    void setAmount_shouldUpdateAmount() {
        // Given
        Operation operation = createTestOperation(OperationType.EXPENSE);
        BigDecimal newAmount = new BigDecimal("99.99");

        // When
        operation.setAmount(newAmount);

        // Then
        assertEquals(newAmount, operation.getAmount());
    }

    @Test
    void getDate_shouldReturnCorrectDate() {
        // Given
        LocalDateTime date = LocalDateTime.of(2023, 5, 15, 10, 30);
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.EXPENSE, UUID.randomUUID(),
                BigDecimal.TEN, date, "Test", UUID.randomUUID()
        );

        // When
        LocalDateTime result = operation.getDate();

        // Then
        assertEquals(date, result);
    }

    @Test
    void setDate_shouldUpdateDate() {
        // Given
        Operation operation = createTestOperation(OperationType.EXPENSE);
        LocalDateTime newDate = LocalDateTime.of(2023, 6, 20, 14, 45);

        // When
        operation.setDate(newDate);

        // Then
        assertEquals(newDate, operation.getDate());
    }

    @Test
    void getDescription_shouldReturnCorrectDescription() {
        // Given
        String description = "Test description";
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.EXPENSE, UUID.randomUUID(),
                BigDecimal.TEN, LocalDateTime.now(), description, UUID.randomUUID()
        );

        // When
        String result = operation.getDescription();

        // Then
        assertEquals(description, result);
    }

    @Test
    void setDescription_shouldUpdateDescription() {
        // Given
        Operation operation = createTestOperation(OperationType.EXPENSE);
        String newDescription = "New description";

        // When
        operation.setDescription(newDescription);

        // Then
        assertEquals(newDescription, operation.getDescription());
    }

    @Test
    void getCategoryId_shouldReturnCorrectId() {
        // Given
        UUID categoryId = UUID.randomUUID();
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.EXPENSE, UUID.randomUUID(),
                BigDecimal.TEN, LocalDateTime.now(), "Test", categoryId
        );

        // When
        UUID result = operation.getCategoryId();

        // Then
        assertEquals(categoryId, result);
    }

    @Test
    void setCategoryId_shouldUpdateCategoryId() {
        // Given
        Operation operation = createTestOperation(OperationType.EXPENSE);
        UUID newCategoryId = UUID.randomUUID();

        // When
        operation.setCategoryId(newCategoryId);

        // Then
        assertEquals(newCategoryId, operation.getCategoryId());
    }

    @Test
    void isIncome_withIncomeType_shouldReturnTrue() {
        // Given
        Operation operation = createTestOperation(OperationType.INCOME);

        // When
        boolean result = operation.isIncome();

        // Then
        assertTrue(result);
    }

    @Test
    void isIncome_withExpenseType_shouldReturnFalse() {
        // Given
        Operation operation = createTestOperation(OperationType.EXPENSE);

        // When
        boolean result = operation.isIncome();

        // Then
        assertFalse(result);
    }

    @Test
    void isExpense_withExpenseType_shouldReturnTrue() {
        // Given
        Operation operation = createTestOperation(OperationType.EXPENSE);

        // When
        boolean result = operation.isExpense();

        // Then
        assertTrue(result);
    }

    @Test
    void isExpense_withIncomeType_shouldReturnFalse() {
        // Given
        Operation operation = createTestOperation(OperationType.INCOME);

        // When
        boolean result = operation.isExpense();

        // Then
        assertFalse(result);
    }

    @Test
    void getSignedAmount_withIncomeType_shouldReturnPositiveAmount() {
        // Given
        BigDecimal amount = new BigDecimal("50.00");
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.INCOME, UUID.randomUUID(),
                amount, LocalDateTime.now(), "Test", UUID.randomUUID()
        );

        // When
        BigDecimal result = operation.getSignedAmount();

        // Then
        assertEquals(amount, result);
    }

    @Test
    void getSignedAmount_withExpenseType_shouldReturnNegativeAmount() {
        // Given
        BigDecimal amount = new BigDecimal("50.00");
        BigDecimal expectedNegativeAmount = new BigDecimal("-50.00");
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.EXPENSE, UUID.randomUUID(),
                amount, LocalDateTime.now(), "Test", UUID.randomUUID()
        );

        // When
        BigDecimal result = operation.getSignedAmount();

        // Then
        assertEquals(expectedNegativeAmount, result);
    }

    @Test
    void toString_withDescription_shouldReturnFormattedString() {
        // Given
        LocalDateTime date = LocalDateTime.of(2023, 5, 15, 10, 30);
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.EXPENSE, UUID.randomUUID(),
                new BigDecimal("75.50"), date, "Grocery shopping", UUID.randomUUID()
        );

        // When
        String result = operation.toString();

        // Then
        assertEquals("EXPENSE: $75.50 on 2023-05-15 (Grocery shopping)", result);
    }

    @Test
    void toString_withNullDescription_shouldReturnFormattedStringWithDefaultDescription() {
        // Given
        LocalDateTime date = LocalDateTime.of(2023, 5, 15, 10, 30);
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.EXPENSE, UUID.randomUUID(),
                new BigDecimal("75.50"), date, null, UUID.randomUUID()
        );

        // When
        String result = operation.toString();

        // Then
        assertEquals("EXPENSE: $75.50 on 2023-05-15 (No description)", result);
    }

    @Test
    void toDetailedString_withDescription_shouldReturnFormattedString() {
        // Given
        UUID id = UUID.randomUUID();
        UUID bankAccountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.of(2023, 5, 15, 10, 30);

        Operation operation = new Operation(
                id, OperationType.EXPENSE, bankAccountId,
                new BigDecimal("75.50"), date, "Grocery shopping", categoryId
        );

        // When
        String result = operation.toDetailedString();

        // Then
        String expected = String.format("""
                        ID: %s
                        Type: EXPENSE
                        Account ID: %s
                        Amount: $75.50
                        Date: 2023-05-15T10:30
                        Category ID: %s
                        Description: Grocery shopping""",
                id, bankAccountId, categoryId);
        assertEquals(expected, result);
    }

    @Test
    void toDetailedString_withNullDescription_shouldReturnFormattedStringWithDefaultDescription() {
        // Given
        UUID id = UUID.randomUUID();
        UUID bankAccountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime date = LocalDateTime.of(2023, 5, 15, 10, 30);

        Operation operation = new Operation(
                id, OperationType.EXPENSE, bankAccountId,
                new BigDecimal("75.50"), date, null, categoryId
        );

        // When
        String result = operation.toDetailedString();

        // Then
        String expected = String.format("""
                        ID: %s
                        Type: EXPENSE
                        Account ID: %s
                        Amount: $75.50
                        Date: 2023-05-15T10:30
                        Category ID: %s
                        Description: No description""",
                id, bankAccountId, categoryId);
        assertEquals(expected, result);
    }

    // Helper method to create test operations
    private Operation createTestOperation(OperationType type) {
        return new Operation(
                UUID.randomUUID(),
                type,
                UUID.randomUUID(),
                BigDecimal.TEN,
                LocalDateTime.now(),
                "Test operation",
                UUID.randomUUID()
        );
    }
}

package com.example.hseshellfinanceapp.domain.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import org.springframework.stereotype.Component;

@Component
public class OperationFactory {
    public Operation createOperation(
            OperationType type,
            UUID bankAccountId,
            BigDecimal amount,
            LocalDateTime date,
            String description,
            UUID categoryId) {

        validateType(type);
        validateBankAccountId(bankAccountId);
        validateAmount(amount);
        validateDate(date);
        validateCategoryId(categoryId);

        return new Operation(
                generateId(),
                type,
                bankAccountId,
                amount,
                date,
                description,
                categoryId
        );
    }

    public Operation createIncome(
            UUID bankAccountId,
            BigDecimal amount,
            LocalDateTime date,
            String description,
            UUID categoryId) {

        return createOperation(
                OperationType.INCOME,
                bankAccountId,
                amount,
                date,
                description,
                categoryId
        );
    }

    public Operation createExpense(
            UUID bankAccountId,
            BigDecimal amount,
            LocalDateTime date,
            String description,
            UUID categoryId) {

        return createOperation(
                OperationType.EXPENSE,
                bankAccountId,
                amount,
                date,
                description,
                categoryId
        );
    }

    private void validateType(OperationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Operation type cannot be null");
        }
    }

    private void validateBankAccountId(UUID bankAccountId) {
        if (bankAccountId == null) {
            throw new IllegalArgumentException("Bank account ID cannot be null");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private void validateDate(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    }

    private void validateCategoryId(UUID categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
    }

    private UUID generateId() {
        return UUID.randomUUID();
    }
}

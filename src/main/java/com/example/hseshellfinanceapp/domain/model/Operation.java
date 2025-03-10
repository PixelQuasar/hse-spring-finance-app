package com.example.hseshellfinanceapp.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Operation {
    private final UUID id;
    private OperationType type;
    private UUID bankAccountId;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private UUID categoryId;

    public Operation(UUID id, OperationType type, UUID bankAccountId, BigDecimal amount,
                     LocalDateTime date, String description, UUID categoryId) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
    }

    public UUID getId() {
        return id;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(UUID bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isIncome() {
        return type == OperationType.INCOME;
    }

    public boolean isExpense() {
        return type == OperationType.EXPENSE;
    }

    public BigDecimal getSignedAmount() {
        if (isExpense()) {
            return amount.negate();
        }
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s: $%.2f on %s (%s)",
                type, amount, date.toLocalDate(),
                description != null ? description : "No description");
    }

    public String toDetailedString() {
        return String.format("""
                        ID: %s
                        Type: %s
                        Account ID: %s
                        Amount: $%.2f
                        Date: %s
                        Category ID: %s
                        Description: %s""",
                id, type, bankAccountId, amount, date, categoryId,
                description != null ? description : "No description");
    }
}

package com.example.hseshellfinanceapp.domain.factory;

import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import org.springframework.stereotype.Component;

@Component
public class CategoryFactory {
    public Category createCategory(String name, OperationType type) {
        validateName(name);
        validateType(type);

        return new Category(generateId(), name, type);
    }

    public Category createIncomeCategory(String name) {
        return createCategory(name, OperationType.INCOME);
    }

    public Category createExpenseCategory(String name) {
        return createCategory(name, OperationType.EXPENSE);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Category name is too long (max 255 characters)");
        }
    }

    private void validateType(OperationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Category type cannot be null");
        }
    }

    private UUID generateId() {
        return UUID.randomUUID();
    }
}

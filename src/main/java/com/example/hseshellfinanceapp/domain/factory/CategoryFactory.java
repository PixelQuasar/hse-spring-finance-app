package com.example.hseshellfinanceapp.domain.factory;

import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import org.springframework.stereotype.Component;

/**
 * Factory for creating Category objects with proper validation
 */
@Component
public class CategoryFactory {

    /**
     * Creates a new category with the specified name and type
     *
     * @param name the name of the category
     * @param type the type of the category (INCOME or EXPENSE)
     * @return a new Category instance
     * @throws IllegalArgumentException if validation fails
     */
    public Category createCategory(String name, OperationType type) {
        validateName(name);
        validateType(type);

        return new Category(generateId(), name, type);
    }

    /**
     * Creates a new income category
     *
     * @param name the name of the income category
     * @return a new Category instance of type INCOME
     */
    public Category createIncomeCategory(String name) {
        return createCategory(name, OperationType.INCOME);
    }

    /**
     * Creates a new expense category
     *
     * @param name the name of the expense category
     * @return a new Category instance of type EXPENSE
     */
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

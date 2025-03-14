package com.example.hseshellfinanceapp.domain.factory;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryFactoryTest {

    private CategoryFactory categoryFactory;

    @BeforeEach
    void setUp() {
        categoryFactory = new CategoryFactory();
    }

    @Test
    void createCategory_WithValidInput_ShouldCreateCategory() {
        // Given
        String name = "Groceries";
        OperationType type = OperationType.EXPENSE;

        // When
        Category category = categoryFactory.createCategory(name, type);

        // Then
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(name, category.getName());
        assertEquals(type, category.getType());
    }

    @Test
    void createIncomeCategory_WithValidName_ShouldCreateIncomeCategory() {
        // Given
        String name = "Salary";

        // When
        Category category = categoryFactory.createIncomeCategory(name);

        // Then
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(name, category.getName());
        assertEquals(OperationType.INCOME, category.getType());
    }

    @Test
    void createExpenseCategory_WithValidName_ShouldCreateExpenseCategory() {
        // Given
        String name = "Utilities";

        // When
        Category category = categoryFactory.createExpenseCategory(name);

        // Then
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(name, category.getName());
        assertEquals(OperationType.EXPENSE, category.getType());
    }

    @Test
    void createCategory_WithNullName_ShouldThrowException() {
        // Given
        String name = null;
        OperationType type = OperationType.INCOME;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryFactory.createCategory(name, type)
        );
        assertEquals("Category name cannot be empty", exception.getMessage());
    }

    @Test
    void createCategory_WithEmptyName_ShouldThrowException() {
        // Given
        String name = "   ";
        OperationType type = OperationType.INCOME;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryFactory.createCategory(name, type)
        );
        assertEquals("Category name cannot be empty", exception.getMessage());
    }

    @Test
    void createCategory_WithTooLongName_ShouldThrowException() {
        // Given
        String name = "a".repeat(256);  // 256 characters
        OperationType type = OperationType.INCOME;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryFactory.createCategory(name, type)
        );
        assertEquals("Category name is too long (max 255 characters)", exception.getMessage());
    }

    @Test
    void createCategory_WithNullType_ShouldThrowException() {
        // Given
        String name = "Utilities";
        OperationType type = null;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryFactory.createCategory(name, type)
        );
        assertEquals("Category type cannot be null", exception.getMessage());
    }

    @Test
    void createIncomeCategory_WithNullName_ShouldThrowException() {
        // Given
        String name = null;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryFactory.createIncomeCategory(name)
        );
        assertEquals("Category name cannot be empty", exception.getMessage());
    }

    @Test
    void createExpenseCategory_WithNullName_ShouldThrowException() {
        // Given
        String name = null;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryFactory.createExpenseCategory(name)
        );
        assertEquals("Category name cannot be empty", exception.getMessage());
    }
}

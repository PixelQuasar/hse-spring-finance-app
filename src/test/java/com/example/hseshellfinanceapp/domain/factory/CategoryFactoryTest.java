package com.example.hseshellfinanceapp.domain.factory;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryFactoryTest {

    private CategoryFactory factory;

    @BeforeEach
    void setUp() {
        factory = new CategoryFactory();
    }

    @Test
    void createCategory_withValidData_shouldCreateCategory() {
        // Given
        String name = "Groceries";
        OperationType type = OperationType.EXPENSE;

        // When
        Category category = factory.createCategory(name, type);

        // Then
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(name, category.getName());
        assertEquals(type, category.getType());
    }

    @Test
    void createIncomeCategory_shouldCreateCategoryWithIncomeType() {
        // Given
        String name = "Salary";

        // When
        Category category = factory.createIncomeCategory(name);

        // Then
        assertNotNull(category);
        assertEquals(name, category.getName());
        assertEquals(OperationType.INCOME, category.getType());
    }

    @Test
    void createExpenseCategory_shouldCreateCategoryWithExpenseType() {
        // Given
        String name = "Rent";

        // When
        Category category = factory.createExpenseCategory(name);

        // Then
        assertNotNull(category);
        assertEquals(name, category.getName());
        assertEquals(OperationType.EXPENSE, category.getType());
    }

    @Test
    void createCategory_withNullName_shouldThrowException() {
        // Given
        String name = null;
        OperationType type = OperationType.EXPENSE;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createCategory(name, type)
        );
        assertEquals("Category name cannot be empty", exception.getMessage());
    }

    @Test
    void createCategory_withEmptyName_shouldThrowException() {
        // Given
        String name = "   ";
        OperationType type = OperationType.EXPENSE;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createCategory(name, type)
        );
        assertEquals("Category name cannot be empty", exception.getMessage());
    }

    @Test
    void createCategory_withTooLongName_shouldThrowException() {
        // Given
        String name = "a".repeat(256); // 256 characters
        OperationType type = OperationType.EXPENSE;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createCategory(name, type)
        );
        assertEquals("Category name is too long (max 255 characters)", exception.getMessage());
    }

    @Test
    void createCategory_withNullType_shouldThrowException() {
        // Given
        String name = "Groceries";
        OperationType type = null;

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createCategory(name, type)
        );
        assertEquals("Category type cannot be null", exception.getMessage());
    }
}

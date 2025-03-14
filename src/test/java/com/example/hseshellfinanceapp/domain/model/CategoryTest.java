package com.example.hseshellfinanceapp.domain.model;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    @Test
    void constructor_shouldInitializeAllFields() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Food";
        OperationType type = OperationType.EXPENSE;

        // When
        Category category = new Category(id, name, type);

        // Then
        assertEquals(id, category.getId());
        assertEquals(name, category.getName());
        assertEquals(type, category.getType());
    }

    @Test
    void getName_shouldReturnCorrectName() {
        // Given
        Category category = new Category(UUID.randomUUID(), "Food", OperationType.EXPENSE);

        // When
        String result = category.getName();

        // Then
        assertEquals("Food", result);
    }

    @Test
    void setName_shouldUpdateName() {
        // Given
        Category category = new Category(UUID.randomUUID(), "Old Name", OperationType.EXPENSE);

        // When
        category.setName("New Name");

        // Then
        assertEquals("New Name", category.getName());
    }

    @Test
    void getType_shouldReturnCorrectType() {
        // Given
        OperationType type = OperationType.INCOME;
        Category category = new Category(UUID.randomUUID(), "Salary", type);

        // When
        OperationType result = category.getType();

        // Then
        assertEquals(type, result);
    }

    @Test
    void setType_shouldUpdateType() {
        // Given
        Category category = new Category(UUID.randomUUID(), "Category", OperationType.INCOME);

        // When
        category.setType(OperationType.EXPENSE);

        // Then
        assertEquals(OperationType.EXPENSE, category.getType());
    }

    @Test
    void toString_shouldReturnFormattedString() {
        // Given
        Category category = new Category(UUID.randomUUID(), "Food", OperationType.EXPENSE);

        // When
        String result = category.toString();

        // Then
        assertEquals("Category: Food (EXPENSE)", result);
    }

    @Test
    void toDetailedString_shouldReturnFormattedString() {
        // Given
        UUID id = UUID.randomUUID();
        Category category = new Category(id, "Food", OperationType.EXPENSE);

        // When
        String result = category.toDetailedString();

        // Then
        String expected = String.format("ID: %s\nName: Food\nType: EXPENSE", id);
        assertEquals(expected, result);
    }
}

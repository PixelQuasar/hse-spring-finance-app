package com.example.hseshellfinanceapp.ui.handler;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.service.command.CommandExecutor;
import com.example.hseshellfinanceapp.service.command.categoryCommands.CreateCategoryCommand;
import com.example.hseshellfinanceapp.service.command.categoryCommands.ListCategoriesCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryHandlerTest {

    @Mock
    private CategoryFacade categoryFacade;
    @Mock
    private CommandExecutor commandExecutor;

    private CategoryHandler categoryHandler;

    @BeforeEach
    void setUp() {
        categoryHandler = new CategoryHandler(categoryFacade, commandExecutor);
    }

    @Test
    void createCategory_shouldReturnSuccessMessage() {
        // Given
        String name = "Groceries";
        OperationType type = OperationType.EXPENSE;
        Category category = new Category(UUID.randomUUID(), name, type);

        when(commandExecutor.executeCommand(any(CreateCategoryCommand.class))).thenReturn(category);

        // When
        String result = categoryHandler.createCategory(name, type);

        // Then
        assertTrue(result.contains("Category created successfully"));
    }

    @Test
    void listCategories_withCategories_shouldDisplayTable() {
        // Given
        Category category = new Category(UUID.randomUUID(), "Groceries", OperationType.EXPENSE);
        List<Category> categories = Collections.singletonList(category);

        when(commandExecutor.executeCommand(any(ListCategoriesCommand.class))).thenReturn(categories);

        // When
        String result = categoryHandler.listCategories(null);

        // Then
        assertTrue(result.contains("All Categories"));
    }
}

package com.example.hseshellfinanceapp.service.command;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.service.command.categoryCommands.CreateCategoryCommand;
import com.example.hseshellfinanceapp.service.command.categoryCommands.DeleteCategoryCommand;
import com.example.hseshellfinanceapp.service.command.categoryCommands.GetCategoryCommand;
import com.example.hseshellfinanceapp.service.command.categoryCommands.ListCategoriesCommand;
import com.example.hseshellfinanceapp.service.command.categoryCommands.UpdateCategoryCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryCommandsTest {

    @Mock
    private CategoryFacade categoryFacade;

    // CreateCategoryCommand tests
    @Test
    void createCategoryCommand_shouldCallFacade() {
        // Given
        String name = "Test Category";
        OperationType type = OperationType.EXPENSE;
        CreateCategoryCommand command = new CreateCategoryCommand(categoryFacade, name, type);
        Category category = new Category(UUID.randomUUID(), name, type);

        when(categoryFacade.createCategory(name, type)).thenReturn(category);

        // When
        Category result = command.execute();

        // Then
        assertSame(category, result);
        verify(categoryFacade).createCategory(name, type);
    }

    // DeleteCategoryCommand tests
    @Test
    void deleteCategoryCommand_shouldCallFacade() {
        // Given
        UUID id = UUID.randomUUID();
        DeleteCategoryCommand command = new DeleteCategoryCommand(categoryFacade, id);

        when(categoryFacade.deleteCategory(id)).thenReturn(true);

        // When
        Boolean result = command.execute();

        // Then
        assertTrue(result);
        verify(categoryFacade).deleteCategory(id);
    }

    // GetCategoryCommand tests
    @Test
    void getCategoryCommand_shouldCallFacade() {
        // Given
        UUID id = UUID.randomUUID();
        GetCategoryCommand command = new GetCategoryCommand(categoryFacade, id);
        Optional<Category> expected = Optional.of(new Category(id, "Test", OperationType.INCOME));

        when(categoryFacade.getCategoryById(id)).thenReturn(expected);

        // When
        Optional<Category> result = command.execute();

        // Then
        assertSame(expected, result);
        verify(categoryFacade).getCategoryById(id);
    }

    // ListCategoriesCommand tests
    @Test
    void listCategoriesCommand_shouldCallFacade() {
        // Given
        ListCategoriesCommand command = new ListCategoriesCommand(categoryFacade);
        List<Category> expected = List.of(new Category(UUID.randomUUID(), "Test", OperationType.EXPENSE));

        when(categoryFacade.getAllCategories()).thenReturn(expected);

        // When
        List<Category> result = command.execute();

        // Then
        assertSame(expected, result);
        verify(categoryFacade).getAllCategories();
    }

    // UpdateCategoryCommand tests
    @Test
    void updateCategoryCommand_shouldCallFacade() {
        // Given
        UUID id = UUID.randomUUID();
        String newName = "New Name";
        UpdateCategoryCommand command = new UpdateCategoryCommand(categoryFacade, id, newName);
        Optional<Category> expected = Optional.of(new Category(id, newName, OperationType.EXPENSE));

        when(categoryFacade.updateCategoryName(id, newName)).thenReturn(expected);

        // When
        Optional<Category> result = command.execute();

        // Then
        assertSame(expected, result);
        verify(categoryFacade).updateCategoryName(id, newName);
    }
}

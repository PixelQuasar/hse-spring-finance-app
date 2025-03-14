package com.example.hseshellfinanceapp.facade;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.factory.CategoryFactory;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.repository.CategoryRepository;
import com.example.hseshellfinanceapp.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryFacadeTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private OperationRepository operationRepository;
    @Mock
    private CategoryFactory categoryFactory;

    private CategoryFacade categoryFacade;

    @BeforeEach
    void setUp() {
        categoryFacade = new CategoryFacade(categoryRepository, operationRepository, categoryFactory);
    }

    @Test
    void createCategory_shouldCreateAndSaveCategory() {
        // Given
        String name = "Groceries";
        OperationType type = OperationType.EXPENSE;
        Category category = new Category(UUID.randomUUID(), name, type);

        when(categoryFactory.createCategory(name, type)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        // When
        Category result = categoryFacade.createCategory(name, type);

        // Then
        assertEquals(category, result);
    }

    @Test
    void updateCategoryName_withExistingId_shouldUpdateAndReturnCategory() {
        // Given
        UUID id = UUID.randomUUID();
        Category category = new Category(id, "Old Name", OperationType.EXPENSE);
        String newName = "New Name";

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Optional<Category> result = categoryFacade.updateCategoryName(id, newName);

        // Then
        assertTrue(result.isPresent());
        assertEquals(newName, result.get().getName());
    }

    @Test
    void deleteCategory_withNoOperations_shouldReturnTrue() {
        // Given
        UUID id = UUID.randomUUID();

        when(operationRepository.findByCategoryId(id)).thenReturn(Collections.emptyList());
        when(categoryRepository.deleteById(id)).thenReturn(true);

        // When
        boolean result = categoryFacade.deleteCategory(id);

        // Then
        assertTrue(result);
    }

    @Test
    void getCategoriesSortedByAmount_shouldReturnSortedList() {
        // Given
        OperationType type = OperationType.EXPENSE;
        Category category1 = new Category(UUID.randomUUID(), "Food", type);
        Category category2 = new Category(UUID.randomUUID(), "Entertainment", type);

        when(categoryRepository.findByType(type)).thenReturn(List.of(category1, category2));
        when(operationRepository.sumByCategoryId(category1.getId())).thenReturn(new BigDecimal("50.00"));
        when(operationRepository.sumByCategoryId(category2.getId())).thenReturn(new BigDecimal("100.00"));

        // When
        List<CategoryFacade.CategorySummary> result = categoryFacade.getCategoriesSortedByAmount(type);

        // Then
        assertEquals(2, result.size());
        assertEquals(category2, result.get(0).getCategory()); // Higher amount first
        assertEquals(category1, result.get(1).getCategory());
    }
}

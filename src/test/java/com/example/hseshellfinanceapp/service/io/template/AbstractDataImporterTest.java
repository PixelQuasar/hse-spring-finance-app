package com.example.hseshellfinanceapp.service.io.template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractDataImporterTest {

    @Mock
    private BankAccountFacade bankAccountFacade;
    @Mock
    private CategoryFacade categoryFacade;
    @Mock
    private OperationFacade operationFacade;

    private TestDataImporter importer;

    @BeforeEach
    void setUp() {
        importer = new TestDataImporter(bankAccountFacade, categoryFacade, operationFacade);
    }

    @Test
    void importData_withValidData_shouldImportAllEntities() {
        // Given
        InputStream inputStream = new ByteArrayInputStream("dummy content".getBytes());
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test Account", new BigDecimal("100.00"));
        Category category = new Category(UUID.randomUUID(), "Groceries", OperationType.EXPENSE);
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.EXPENSE, account.getId(),
                new BigDecimal("50.00"), LocalDateTime.now(), "Test expense", category.getId());

        importer.setTestData(List.of(account), List.of(category), List.of(operation));

        when(bankAccountFacade.createAccount(anyString(), any(BigDecimal.class))).thenReturn(account);
        when(categoryFacade.createCategory(anyString(), any(OperationType.class))).thenReturn(category);
        when(operationFacade.createOperation(any(), any(), any(), any(), anyString())).thenReturn(Optional.of(operation));

        // When
        AbstractDataImporter.ImportResult result = importer.importData(inputStream);

        // Then
        assertTrue(result.success());
        assertEquals(1, result.accountsImported());
        assertEquals(1, result.categoriesImported());
        assertEquals(1, result.operationsImported());

        verify(bankAccountFacade).createAccount(account.getName(), account.getBalance());
        verify(categoryFacade).createCategory(category.getName(), category.getType());
        verify(operationFacade).createOperation(
                operation.getType(),
                operation.getBankAccountId(),
                operation.getCategoryId(),
                operation.getAmount(),
                operation.getDescription()
        );
    }

    @Test
    void importData_withParsingError_shouldReturnFailureResult() {
        // Given
        InputStream inputStream = new ByteArrayInputStream("dummy content".getBytes());
        importer.setShouldThrowParsingException(true);

        // When
        AbstractDataImporter.ImportResult result = importer.importData(inputStream);

        // Then
        assertFalse(result.success());
        assertTrue(result.message().contains("Import failed"));
        assertEquals(0, result.accountsImported());
        assertEquals(0, result.categoriesImported());
        assertEquals(0, result.operationsImported());

        verifyNoInteractions(bankAccountFacade, categoryFacade, operationFacade);
    }

    // Test implementation that can return predefined data or throw exceptions
    private static class TestDataImporter extends AbstractDataImporter {
        private List<BankAccount> accounts;
        private List<Category> categories;
        private List<Operation> operations;
        private boolean shouldThrowParsingException = false;

        public TestDataImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade,
                                OperationFacade operationFacade) {
            super(bankAccountFacade, categoryFacade, operationFacade);
        }

        public void setTestData(List<BankAccount> accounts, List<Category> categories, List<Operation> operations) {
            this.accounts = accounts;
            this.categories = categories;
            this.operations = operations;
        }

        public void setShouldThrowParsingException(boolean shouldThrow) {
            this.shouldThrowParsingException = shouldThrow;
        }

        @Override
        protected ImportData parseData(InputStream inputStream) throws Exception {
            if (shouldThrowParsingException) {
                throw new Exception("Test parsing exception");
            }
            return new ImportData(accounts, categories, operations);
        }
    }
}

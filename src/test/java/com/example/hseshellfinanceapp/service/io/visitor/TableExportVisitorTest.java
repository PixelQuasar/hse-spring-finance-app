package com.example.hseshellfinanceapp.service.io.visitor;

import java.io.ByteArrayOutputStream;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableExportVisitorTest {

    @Mock
    private BankAccountFacade bankAccountFacade;
    @Mock
    private CategoryFacade categoryFacade;
    @Mock
    private OperationFacade operationFacade;

    private TableExportVisitor visitor;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        visitor = new TableExportVisitor(bankAccountFacade, categoryFacade, operationFacade);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void visitAccounts_withAccounts_shouldGenerateTable() throws Exception {
        // Given
        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, "Test Account", new BigDecimal("100.00"));
        List<BankAccount> accounts = List.of(account);

        // When
        visitor.startExport(outputStream);
        visitor.visitAccounts(accounts, outputStream);
        visitor.endExport(outputStream);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("== ACCOUNTS =="));
        assertTrue(output.contains("ID"));
        assertTrue(output.contains("Name"));
        assertTrue(output.contains("Balance"));
        assertTrue(output.contains(accountId.toString()));
        assertTrue(output.contains("Test Account"));
        assertTrue(output.contains("100.00"));
    }

    @Test
    void visitCategories_withCategories_shouldGenerateTable() throws Exception {
        // Given
        UUID categoryId = UUID.randomUUID();
        Category category = new Category(categoryId, "Groceries", OperationType.EXPENSE);
        List<Category> categories = List.of(category);

        // When
        visitor.startExport(outputStream);
        visitor.visitCategories(categories, outputStream);
        visitor.endExport(outputStream);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("== CATEGORIES =="));
        assertTrue(output.contains("ID"));
        assertTrue(output.contains("Name"));
        assertTrue(output.contains("Type"));
        assertTrue(output.contains(categoryId.toString()));
        assertTrue(output.contains("Groceries"));
        assertTrue(output.contains("EXPENSE"));
    }

    @Test
    void visitOperations_withOperations_shouldGenerateTable() throws Exception {
        // Given
        UUID operationId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        BankAccount account = new BankAccount(accountId, "Test Account", BigDecimal.ZERO);
        Category category = new Category(categoryId, "Groceries", OperationType.EXPENSE);

        Operation operation = new Operation(
                operationId, OperationType.EXPENSE, accountId,
                new BigDecimal("50.00"), LocalDateTime.now(), "Test expense", categoryId);

        List<Operation> operations = List.of(operation);

        when(bankAccountFacade.getAccountById(accountId)).thenReturn(Optional.of(account));
        when(categoryFacade.getCategoryById(categoryId)).thenReturn(Optional.of(category));

        // When
        visitor.startExport(outputStream);
        visitor.visitOperations(operations, outputStream);
        visitor.endExport(outputStream);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("== OPERATIONS =="));
        assertTrue(output.contains("ID"));
        assertTrue(output.contains("Type"));
        assertTrue(output.contains("Account"));
        assertTrue(output.contains("Amount"));
        assertTrue(output.contains("Date"));
        assertTrue(output.contains("Category"));
        assertTrue(output.contains(operationId.toString()));
        assertTrue(output.contains("EXPENSE"));
        assertTrue(output.contains("Test Account"));
        assertTrue(output.contains("50.00"));
        assertTrue(output.contains("Groceries"));
    }
}

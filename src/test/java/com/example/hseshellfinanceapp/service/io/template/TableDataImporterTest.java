package com.example.hseshellfinanceapp.service.io.template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TableDataImporterTest {

    @Mock
    private BankAccountFacade bankAccountFacade;
    @Mock
    private CategoryFacade categoryFacade;
    @Mock
    private OperationFacade operationFacade;

    private TableDataImporter importer;

    @BeforeEach
    void setUp() {
        importer = new TableDataImporter(bankAccountFacade, categoryFacade, operationFacade);
    }

    @Test
    void parseData_withValidTableData_shouldReturnImportData() throws Exception {
        // Given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(formatter);

        StringBuilder tableData = new StringBuilder();

        // Accounts table
        tableData.append("== ACCOUNTS ==\n");
        tableData.append("| ID | Name | Balance |\n");
        tableData.append("|----|------|--------|\n");
        tableData.append("| 123 | Test Account | 100.00 |\n\n");

        // Categories table
        tableData.append("== CATEGORIES ==\n");
        tableData.append("| ID | Name | Type |\n");
        tableData.append("|----|------|------|\n");
        tableData.append("| 456 | Groceries | EXPENSE |\n\n");

        // Operations table
        tableData.append("== OPERATIONS ==\n");
        tableData.append("| ID | Type | Account | Amount | Date | Category | Description |\n");
        tableData.append("|----|----|-------|-------|------|----------|-------------|\n");
        tableData.append("| 789 | EXPENSE | Test Account | 50.00 | " + dateStr + " | Groceries | Test expense |\n");

        InputStream inputStream = new ByteArrayInputStream(tableData.toString().getBytes());

        // When
        AbstractDataImporter.ImportData data = importer.parseData(inputStream);

        // Then
        assertNotNull(data);
        assertEquals(1, data.accounts().size());
        assertEquals(1, data.categories().size());
        assertEquals(1, data.operations().size());

        BankAccount account = data.accounts().get(0);
        assertEquals("Test Account", account.getName());
        assertEquals(new BigDecimal("100.00"), account.getBalance());

        Category category = data.categories().get(0);
        assertEquals("Groceries", category.getName());
        assertEquals(OperationType.EXPENSE, category.getType());

        Operation operation = data.operations().get(0);
        assertEquals(OperationType.EXPENSE, operation.getType());
        assertEquals(new BigDecimal("50.00"), operation.getAmount());
        assertEquals("Test expense", operation.getDescription());
    }

    @Test
    void parseData_withEmptyInput_shouldReturnEmptyImportData() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream("".getBytes());

        // When
        AbstractDataImporter.ImportData data = importer.parseData(inputStream);

        // Then
        assertNotNull(data);
        assertTrue(data.accounts().isEmpty());
        assertTrue(data.categories().isEmpty());
        assertTrue(data.operations().isEmpty());
    }
}

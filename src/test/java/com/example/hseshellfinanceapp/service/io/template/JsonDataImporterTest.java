package com.example.hseshellfinanceapp.service.io.template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JsonDataImporterTest {

    @Mock
    private BankAccountFacade bankAccountFacade;
    @Mock
    private CategoryFacade categoryFacade;
    @Mock
    private OperationFacade operationFacade;

    private JsonDataImporter importer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        importer = new JsonDataImporter(bankAccountFacade, categoryFacade, operationFacade);
        objectMapper = new ObjectMapper();
    }

    @Test
    void parseData_withValidJson_shouldReturnImportData() throws Exception {
        // Given
        ObjectNode rootNode = objectMapper.createObjectNode();

        // Create accounts array
        ArrayNode accountsArray = rootNode.putArray("accounts");
        ObjectNode accountNode = accountsArray.addObject();
        accountNode.put("name", "Test Account");
        accountNode.put("balance", "100.00");

        // Create categories array
        ArrayNode categoriesArray = rootNode.putArray("categories");
        ObjectNode categoryNode = categoriesArray.addObject();
        categoryNode.put("name", "Groceries");
        categoryNode.put("type", "EXPENSE");

        // Create operations array
        ArrayNode operationsArray = rootNode.putArray("operations");
        ObjectNode operationNode = operationsArray.addObject();
        operationNode.put("type", "EXPENSE");
        operationNode.put("accountName", "Test Account");
        operationNode.put("categoryName", "Groceries");
        operationNode.put("amount", "50.00");
        operationNode.put("date", LocalDateTime.now().toString());
        operationNode.put("description", "Test expense");

        String jsonContent = objectMapper.writeValueAsString(rootNode);
        InputStream inputStream = new ByteArrayInputStream(jsonContent.getBytes());

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
    void parseData_withInvalidJson_shouldThrowException() {
        // Given
        String invalidJson = "{invalid json";
        InputStream inputStream = new ByteArrayInputStream(invalidJson.getBytes());

        // When/Then
        assertThrows(Exception.class, () -> importer.parseData(inputStream));
    }
}

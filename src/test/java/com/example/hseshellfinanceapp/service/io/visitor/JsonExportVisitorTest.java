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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonExportVisitorTest {

    @Mock
    private BankAccountFacade bankAccountFacade;
    @Mock
    private CategoryFacade categoryFacade;
    @Mock
    private OperationFacade operationFacade;

    private JsonExportVisitor visitor;
    private ByteArrayOutputStream outputStream;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        visitor = new JsonExportVisitor(bankAccountFacade, categoryFacade, operationFacade);
        outputStream = new ByteArrayOutputStream();
        objectMapper = new ObjectMapper();
    }

    @Test
    void exportData_shouldCreateValidJson() throws Exception {
        // Given
        UUID accountId = UUID.randomUUID();
        BankAccount account = new BankAccount(accountId, "Test Account", new BigDecimal("100.00"));

        UUID categoryId = UUID.randomUUID();
        Category category = new Category(categoryId, "Groceries", OperationType.EXPENSE);

        UUID operationId = UUID.randomUUID();
        Operation operation = new Operation(
                operationId, OperationType.EXPENSE, accountId,
                new BigDecimal("50.00"), LocalDateTime.now(), "Test expense", categoryId);

        when(bankAccountFacade.getAllAccounts()).thenReturn(List.of(account));
        when(categoryFacade.getAllCategories()).thenReturn(List.of(category));
        when(operationFacade.getAllOperations()).thenReturn(List.of(operation));
        when(bankAccountFacade.getAccountById(accountId)).thenReturn(Optional.of(account));
        when(categoryFacade.getCategoryById(categoryId)).thenReturn(Optional.of(category));

        // When
        visitor.exportData(outputStream, true, true, true);

        // Then
        String jsonOutput = outputStream.toString();
        JsonNode rootNode = objectMapper.readTree(jsonOutput);

        assertTrue(rootNode.has("accounts"));
        assertTrue(rootNode.has("categories"));
        assertTrue(rootNode.has("operations"));

        assertEquals(1, rootNode.get("accounts").size());
        assertEquals(1, rootNode.get("categories").size());
        assertEquals(1, rootNode.get("operations").size());

        JsonNode accountNode = rootNode.get("accounts").get(0);
        assertEquals(accountId.toString(), accountNode.get("id").asText());
        assertEquals("Test Account", accountNode.get("name").asText());
        assertEquals("100.00", accountNode.get("balance").asText());

        JsonNode categoryNode = rootNode.get("categories").get(0);
        assertEquals(categoryId.toString(), categoryNode.get("id").asText());
        assertEquals("Groceries", categoryNode.get("name").asText());
        assertEquals("EXPENSE", categoryNode.get("type").asText());

        JsonNode operationNode = rootNode.get("operations").get(0);
        assertEquals(operationId.toString(), operationNode.get("id").asText());
        assertEquals("EXPENSE", operationNode.get("type").asText());
        assertEquals(accountId.toString(), operationNode.get("bankAccountId").asText());
        assertEquals("50.00", operationNode.get("amount").asText());
        assertEquals("Test expense", operationNode.get("description").asText());
        assertEquals(categoryId.toString(), operationNode.get("categoryId").asText());
    }
}

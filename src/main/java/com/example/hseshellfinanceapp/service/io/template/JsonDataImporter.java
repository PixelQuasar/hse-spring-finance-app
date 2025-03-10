package com.example.hseshellfinanceapp.service.io.template;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class JsonDataImporter extends AbstractDataImporter {

    private final ObjectMapper objectMapper;

    public JsonDataImporter(
            BankAccountFacade bankAccountFacade,
            CategoryFacade categoryFacade,
            OperationFacade operationFacade) {
        super(bankAccountFacade, categoryFacade, operationFacade);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected ImportData parseData(InputStream inputStream) throws Exception {
        JsonNode rootNode = objectMapper.readTree(inputStream);

        List<BankAccount> accounts = parseAccounts(rootNode.path("accounts"));
        List<Category> categories = parseCategories(rootNode.path("categories"));
        List<Operation> operations = parseOperations(rootNode.path("operations"));

        return new ImportData(accounts, categories, operations);
    }

    private List<BankAccount> parseAccounts(JsonNode accountsNode) {
        List<BankAccount> accounts = new ArrayList<>();

        if (accountsNode.isArray()) {
            ArrayNode accountsArray = (ArrayNode) accountsNode;
            for (JsonNode accountNode : accountsArray) {
                try {
                    UUID id = UUID.randomUUID();
                    String name = accountNode.path("name").asText();
                    BigDecimal balance = new BigDecimal(accountNode.path("balance").asText("0.0"));

                    accounts.add(new BankAccount(id, name, balance));
                } catch (Exception e) {
                    System.err.println("Error parsing account: " + e.getMessage());
                }
            }
        }

        return accounts;
    }

    private List<Category> parseCategories(JsonNode categoriesNode) {
        List<Category> categories = new ArrayList<>();

        if (categoriesNode.isArray()) {
            ArrayNode categoriesArray = (ArrayNode) categoriesNode;
            for (JsonNode categoryNode : categoriesArray) {
                try {
                    UUID id = UUID.randomUUID();
                    String name = categoryNode.path("name").asText();
                    String typeStr = categoryNode.path("type").asText();

                    com.example.hseshellfinanceapp.domain.model.OperationType type =
                            com.example.hseshellfinanceapp.domain.model.OperationType.valueOf(typeStr);

                    categories.add(new Category(id, name, type));
                } catch (Exception e) {
                    System.err.println("Error parsing category: " + e.getMessage());
                }
            }
        }

        return categories;
    }

    private List<Operation> parseOperations(JsonNode operationsNode) {
        List<Operation> operations = new ArrayList<>();

        if (operationsNode.isArray()) {
            ArrayNode operationsArray = (ArrayNode) operationsNode;
            for (JsonNode operationNode : operationsArray) {
                try {
                    UUID id = UUID.randomUUID();
                    String typeStr = operationNode.path("type").asText();
                    com.example.hseshellfinanceapp.domain.model.OperationType type =
                            com.example.hseshellfinanceapp.domain.model.OperationType.valueOf(typeStr);

                    String accountName = operationNode.path("accountName").asText();
                    String categoryName = operationNode.path("categoryName").asText();

                    UUID bankAccountId = UUID.randomUUID();
                    UUID categoryId = UUID.randomUUID();

                    BigDecimal amount = new BigDecimal(operationNode.path("amount").asText("0.0"));
                    LocalDateTime date = LocalDateTime.parse(operationNode.path("date").asText());
                    String description = operationNode.path("description").asText("");

                    operations.add(new Operation(id, type, bankAccountId, amount, date, description, categoryId));
                } catch (Exception e) {
                    System.err.println("Error parsing operation: " + e.getMessage());
                }
            }
        }

        return operations;
    }
}

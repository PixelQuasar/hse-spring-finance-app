package com.example.hseshellfinanceapp.service.io.visitor;

import java.io.OutputStream;
import java.util.List;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class JsonExportVisitor extends AbstractExportVisitor {

    private final ObjectMapper objectMapper;
    private ObjectNode rootNode;

    public JsonExportVisitor(
            BankAccountFacade bankAccountFacade,
            CategoryFacade categoryFacade,
            OperationFacade operationFacade) {
        super(bankAccountFacade, categoryFacade, operationFacade);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void startExport(OutputStream outputStream) {
        rootNode = objectMapper.createObjectNode();
    }

    @Override
    protected void visitAccounts(List<BankAccount> accounts, OutputStream outputStream) {
        ArrayNode accountsArray = rootNode.putArray("accounts");

        for (BankAccount account : accounts) {
            ObjectNode accountNode = accountsArray.addObject();
            accountNode.put("id", account.getId().toString());
            accountNode.put("name", account.getName());
            accountNode.put("balance", account.getBalance());
        }
    }

    @Override
    protected void visitCategories(List<Category> categories, OutputStream outputStream) {
        ArrayNode categoriesArray = rootNode.putArray("categories");

        for (Category category : categories) {
            ObjectNode categoryNode = categoriesArray.addObject();
            categoryNode.put("id", category.getId().toString());
            categoryNode.put("name", category.getName());
            categoryNode.put("type", category.getType().name());
        }
    }

    @Override
    protected void visitOperations(List<Operation> operations, OutputStream outputStream) {
        ArrayNode operationsArray = rootNode.putArray("operations");

        for (Operation operation : operations) {
            ObjectNode operationNode = operationsArray.addObject();
            operationNode.put("id", operation.getId().toString());
            operationNode.put("type", operation.getType().name());
            operationNode.put("bankAccountId", operation.getBankAccountId().toString());
            operationNode.put("amount", operation.getAmount());
            operationNode.put("date", operation.getDate().toString());
            operationNode.put("description", operation.getDescription());
            operationNode.put("categoryId", operation.getCategoryId().toString());

            bankAccountFacade.getAccountById(operation.getBankAccountId())
                    .ifPresent(account -> operationNode.put("accountName", account.getName()));

            categoryFacade.getCategoryById(operation.getCategoryId())
                    .ifPresent(category -> operationNode.put("categoryName", category.getName()));
        }
    }

    @Override
    protected void endExport(OutputStream outputStream) throws Exception {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, rootNode);
    }
}

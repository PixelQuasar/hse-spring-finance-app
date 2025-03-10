package com.example.hseshellfinanceapp.service.io.template;

import java.io.InputStream;
import java.util.List;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public abstract class AbstractDataImporter {

    protected final BankAccountFacade bankAccountFacade;
    protected final CategoryFacade categoryFacade;
    protected final OperationFacade operationFacade;

    @Autowired
    public AbstractDataImporter(
            BankAccountFacade bankAccountFacade,
            CategoryFacade categoryFacade,
            OperationFacade operationFacade) {
        this.bankAccountFacade = bankAccountFacade;
        this.categoryFacade = categoryFacade;
        this.operationFacade = operationFacade;
    }

    @Transactional
    public ImportResult importData(InputStream inputStream) {
        try {
            ImportData data = parseData(inputStream);
            validateData(data);

            int accountsImported = importAccounts(data.accounts());
            int categoriesImported = importCategories(data.categories());
            int operationsImported = importOperations(data.operations());

            return new ImportResult(
                    true,
                    "Import successful",
                    accountsImported,
                    categoriesImported,
                    operationsImported
            );
        } catch (Exception e) {
            return new ImportResult(
                    false,
                    "Import failed: " + e.getMessage(),
                    0, 0, 0
            );
        }
    }

    protected abstract ImportData parseData(InputStream inputStream) throws Exception;

    protected void validateData(ImportData data) throws Exception {
        if (data == null) {
            throw new Exception("Imported data is null");
        }
    }

    private int importAccounts(List<BankAccount> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (BankAccount account : accounts) {
            try {
                bankAccountFacade.createAccount(account.getName(), account.getBalance());
                count++;
            } catch (Exception e) {
                System.err.println("Error importing account: " + e.getMessage());
            }
        }
        return count;
    }

    private int importCategories(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Category category : categories) {
            try {
                categoryFacade.createCategory(category.getName(), category.getType());
                count++;
            } catch (Exception e) {
                System.err.println("Error importing category: " + e.getMessage());
            }
        }
        return count;
    }

    private int importOperations(List<Operation> operations) {
        if (operations == null || operations.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Operation operation : operations) {
            try {
                operationFacade.createOperation(
                        operation.getType(),
                        operation.getBankAccountId(),
                        operation.getCategoryId(),
                        operation.getAmount(),
                        operation.getDescription()
                );
                count++;
            } catch (Exception e) {
                System.err.println("Error importing operation: " + e.getMessage());
            }
        }
        return count;
    }

    public record ImportData(
            List<BankAccount> accounts,
            List<Category> categories,
            List<Operation> operations
    ) {
    }

    public record ImportResult(
            boolean success,
            String message,
            int accountsImported,
            int categoriesImported,
            int operationsImported
    ) {
        @Override
        public String toString() {
            if (success) {
                return String.format(
                        "Import successful: %d accounts, %d categories, %d operations imported",
                        accountsImported, categoriesImported, operationsImported
                );
            } else {
                return "Import failed: " + message;
            }
        }
    }
}

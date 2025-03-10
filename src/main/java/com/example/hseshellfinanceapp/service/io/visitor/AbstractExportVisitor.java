package com.example.hseshellfinanceapp.service.io.visitor;

import java.io.OutputStream;
import java.util.List;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractExportVisitor {

    protected final BankAccountFacade bankAccountFacade;
    protected final CategoryFacade categoryFacade;
    protected final OperationFacade operationFacade;

    @Autowired
    public AbstractExportVisitor(
            BankAccountFacade bankAccountFacade,
            CategoryFacade categoryFacade,
            OperationFacade operationFacade) {
        this.bankAccountFacade = bankAccountFacade;
        this.categoryFacade = categoryFacade;
        this.operationFacade = operationFacade;
    }

    public void exportData(OutputStream outputStream, boolean includeAccounts,
                           boolean includeCategories, boolean includeOperations) throws Exception {
        List<BankAccount> accounts = includeAccounts ? bankAccountFacade.getAllAccounts() : List.of();
        List<Category> categories = includeCategories ? categoryFacade.getAllCategories() : List.of();
        List<Operation> operations = includeOperations ? operationFacade.getAllOperations() : List.of();

        startExport(outputStream);

        if (includeAccounts) {
            visitAccounts(accounts, outputStream);
        }

        if (includeCategories) {
            visitCategories(categories, outputStream);
        }

        if (includeOperations) {
            visitOperations(operations, outputStream);
        }

        endExport(outputStream);
    }

    protected abstract void startExport(OutputStream outputStream) throws Exception;

    protected abstract void visitAccounts(List<BankAccount> accounts, OutputStream outputStream) throws Exception;

    protected abstract void visitCategories(List<Category> categories, OutputStream outputStream) throws Exception;

    protected abstract void visitOperations(List<Operation> operations, OutputStream outputStream) throws Exception;

    protected abstract void endExport(OutputStream outputStream) throws Exception;
}

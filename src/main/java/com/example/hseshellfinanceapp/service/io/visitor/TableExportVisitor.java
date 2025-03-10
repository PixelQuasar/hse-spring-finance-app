package com.example.hseshellfinanceapp.service.io.visitor;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import org.springframework.stereotype.Component;

@Component
public class TableExportVisitor extends AbstractExportVisitor {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private PrintWriter writer;

    public TableExportVisitor(
            BankAccountFacade bankAccountFacade,
            CategoryFacade categoryFacade,
            OperationFacade operationFacade) {
        super(bankAccountFacade, categoryFacade, operationFacade);
    }

    @Override
    protected void startExport(OutputStream outputStream) {
        writer = new PrintWriter(new OutputStreamWriter(outputStream));
    }

    @Override
    protected void visitAccounts(List<BankAccount> accounts, OutputStream outputStream) {
        if (accounts.isEmpty()) {
            return;
        }

        writer.println("== ACCOUNTS ==");

        List<String> headers = List.of("ID", "Name", "Balance");
        List<List<String>> rows = new ArrayList<>();

        for (BankAccount account : accounts) {
            List<String> row = List.of(
                    account.getId().toString(),
                    account.getName(),
                    account.getBalance().toString()
            );
            rows.add(row);
        }

        printTable(headers, rows);
        writer.println();
    }

    @Override
    protected void visitCategories(List<Category> categories, OutputStream outputStream) {
        if (categories.isEmpty()) {
            return;
        }

        writer.println("== CATEGORIES ==");

        List<String> headers = List.of("ID", "Name", "Type");
        List<List<String>> rows = new ArrayList<>();

        for (Category category : categories) {
            List<String> row = List.of(
                    category.getId().toString(),
                    category.getName(),
                    category.getType().name()
            );
            rows.add(row);
        }

        printTable(headers, rows);
        writer.println();
    }

    @Override
    protected void visitOperations(List<Operation> operations, OutputStream outputStream) {
        if (operations.isEmpty()) {
            return;
        }

        writer.println("== OPERATIONS ==");

        List<String> headers = List.of("ID", "Type", "Account", "Amount", "Date", "Category", "Description");
        List<List<String>> rows = new ArrayList<>();

        for (Operation operation : operations) {
            String accountName = bankAccountFacade.getAccountById(operation.getBankAccountId())
                    .map(BankAccount::getName)
                    .orElse("-");

            String categoryName = categoryFacade.getCategoryById(operation.getCategoryId())
                    .map(Category::getName)
                    .orElse("-");

            List<String> row = List.of(
                    operation.getId().toString(),
                    operation.getType().name(),
                    accountName,
                    operation.getAmount().toString(),
                    operation.getDate().format(DATE_FORMATTER),
                    categoryName,
                    operation.getDescription() != null ? operation.getDescription() : ""
            );
            rows.add(row);
        }

        printTable(headers, rows);
    }

    @Override
    protected void endExport(OutputStream outputStream) {
        writer.flush();
    }

    private void printTable(List<String> headers, List<List<String>> rows) {
        List<Integer> columnWidths = calculateColumnWidths(headers, rows);

        printRow(headers, columnWidths);
        printSeparator(columnWidths);

        for (List<String> row : rows) {
            printRow(row, columnWidths);
        }
    }

    private void printRow(List<String> cells, List<Integer> columnWidths) {
        StringBuilder sb = new StringBuilder("|");

        for (int i = 0; i < cells.size(); i++) {
            String cell = cells.get(i);
            int width = columnWidths.get(i);

            sb.append(" ").append(padRight(cell, width)).append(" |");
        }

        writer.println(sb.toString());
    }

    private void printSeparator(List<Integer> columnWidths) {
        StringBuilder sb = new StringBuilder("|");

        for (int width : columnWidths) {
            sb.append("-").append(repeat("-", width)).append("-|");
        }

        writer.println(sb.toString());
    }

    private List<Integer> calculateColumnWidths(List<String> headers, List<List<String>> rows) {
        List<Integer> widths = new ArrayList<>(headers.size());

        for (int i = 0; i < headers.size(); i++) {
            int maxWidth = headers.get(i).length();

            for (List<String> row : rows) {
                if (i < row.size()) {
                    maxWidth = Math.max(maxWidth, row.get(i).length());
                }
            }

            widths.add(maxWidth);
        }

        return widths;
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private String repeat(String s, int n) {
        return new String(new char[n]).replace("\0", s);
    }
}

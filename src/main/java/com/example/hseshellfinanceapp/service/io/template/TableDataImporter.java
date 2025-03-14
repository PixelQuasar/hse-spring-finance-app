package com.example.hseshellfinanceapp.service.io.template;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import org.springframework.stereotype.Component;

@Component
public class TableDataImporter extends AbstractDataImporter {

    private static final String TABLE_SEPARATOR = "|-";
    private static final String SECTION_MARKER = "==";

    public TableDataImporter(
            BankAccountFacade bankAccountFacade,
            CategoryFacade categoryFacade,
            OperationFacade operationFacade) {
        super(bankAccountFacade, categoryFacade, operationFacade);
    }

    @Override
    protected ImportData parseData(InputStream inputStream) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<BankAccount> accounts = new ArrayList<>();
            List<Category> categories = new ArrayList<>();
            List<Operation> operations = new ArrayList<>();

            String currentSection = null;
            List<String> headers = null;
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith(SECTION_MARKER)) {
                    currentSection = line.replace(SECTION_MARKER, "").trim().toLowerCase();
                    headers = null;
                    continue;
                }

                if (line.startsWith(TABLE_SEPARATOR)) {
                    continue;
                }

                if (headers == null) {
                    headers = parseRow(line);
                    continue;
                }

                List<String> values = parseRow(line);

                if (values.size() != headers.size()) {
                    continue;
                }

                if ("accounts".equals(currentSection)) {
                    accounts.add(parseBankAccount(headers, values));
                } else if ("categories".equals(currentSection)) {
                    categories.add(parseCategory(headers, values));
                } else if ("operations".equals(currentSection)) {
                    operations.add(parseOperation(headers, values));
                }
            }

            return new ImportData(accounts, categories, operations);
        }
    }

    private List<String> parseRow(String line) {
        List<String> cells = new ArrayList<>();

        String[] parts = line.split("\\|");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                cells.add(trimmed);
            }
        }

        return cells;
    }

    private BankAccount parseBankAccount(List<String> headers, List<String> values) {
        String name = null;
        BigDecimal balance = BigDecimal.ZERO;

        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i).toLowerCase();
            String value = values.get(i);

            if ("name".equals(header)) {
                name = value;
            } else if ("balance".equals(header)) {
                balance = new BigDecimal(value);
            }
        }

        return new BankAccount(UUID.randomUUID(), name, balance);
    }

    private Category parseCategory(List<String> headers, List<String> values) {
        String name = null;
        OperationType type = null;

        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i).toLowerCase();
            String value = values.get(i);

            if ("name".equals(header)) {
                name = value;
            } else if ("type".equals(header)) {
                type = OperationType.valueOf(value.toUpperCase());
            }
        }

        return new Category(UUID.randomUUID(), name, type);
    }

    private Operation parseOperation(List<String> headers, List<String> values) {
        OperationType type = null;
        UUID bankAccountId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.ZERO;
        LocalDateTime date = LocalDateTime.now();
        String description = "";
        UUID categoryId = UUID.randomUUID();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i).toLowerCase();
            String value = values.get(i);

            switch (header) {
                case "type":
                    type = OperationType.valueOf(value.toUpperCase());
                    break;
                case "account":
                case "accountname":
                    break;
                case "amount":
                    amount = new BigDecimal(value);
                    break;
                case "date":
                    date = LocalDateTime.parse(value, formatter);
                    break;
                case "description":
                    description = value;
                    break;
                case "category":
                default:
                    break;
            }
        }

        return new Operation(UUID.randomUUID(), type, bankAccountId, amount, date, description, categoryId);
    }
}

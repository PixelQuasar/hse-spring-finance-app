package com.example.hseshellfinanceapp.ui.handler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.example.hseshellfinanceapp.service.command.CommandExecutor;
import com.example.hseshellfinanceapp.service.command.operationCommands.CreateExpenseCommand;
import com.example.hseshellfinanceapp.service.command.operationCommands.CreateIncomeCommand;
import com.example.hseshellfinanceapp.service.command.operationCommands.DeleteOperationCommand;
import com.example.hseshellfinanceapp.service.command.operationCommands.GetOperationDetailsCommand;
import com.example.hseshellfinanceapp.service.command.operationCommands.ListOperationsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

@ShellComponent
public class OperationHandler {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final OperationFacade operationFacade;
    private final CommandExecutor commandExecutor;

    @Autowired
    public OperationHandler(OperationFacade operationFacade, CommandExecutor commandExecutor) {
        this.operationFacade = operationFacade;
        this.commandExecutor = commandExecutor;
    }

    @ShellMethod(value = "Create a new income operation", key = "add-income")
    public String createIncome(
            @ShellOption(help = "Account ID") UUID accountId,
            @ShellOption(help = "Category ID") UUID categoryId,
            @ShellOption(help = "Amount") BigDecimal amount,
            @ShellOption(help = "Description", defaultValue = "") String description) {
        try {
            CreateIncomeCommand command = new CreateIncomeCommand(
                    operationFacade, accountId, categoryId, amount, LocalDateTime.now(), description);
            Optional<Operation> operationOpt = commandExecutor.executeCommand(command);

            return operationOpt.map(operation -> "Income added successfully: " + operation.toString())
                    .orElse("Failed to add income. Check account and category IDs.");
        } catch (Exception e) {
            return "Error adding income: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Create a new expense operation", key = "add-expense")
    public String createExpense(
            @ShellOption(help = "Account ID") UUID accountId,
            @ShellOption(help = "Category ID") UUID categoryId,
            @ShellOption(help = "Amount") BigDecimal amount,
            @ShellOption(help = "Description", defaultValue = "") String description) {
        try {
            CreateExpenseCommand command = new CreateExpenseCommand(
                    operationFacade, accountId, categoryId, amount, LocalDateTime.now(), description);
            Optional<Operation> operationOpt = commandExecutor.executeCommand(command);

            return operationOpt.map(operation -> "Expense added successfully: " + operation.toString())
                    .orElse("Failed to add expense. Check account and category IDs, and ensure sufficient funds.");
        } catch (Exception e) {
            return "Error adding expense: " + e.getMessage();
        }
    }

    @ShellMethod(value = "List operations", key = "list-operations")
    public String listOperations(
            @ShellOption(help = "Account ID", defaultValue = ShellOption.NULL) UUID accountId,
            @ShellOption(help = "Operation type (INCOME or EXPENSE)", defaultValue = ShellOption.NULL)
            OperationType typeFilter,
            @ShellOption(help = "Start date (yyyy-MM-dd)", defaultValue = ShellOption.NULL) LocalDate fromDate,
            @ShellOption(help = "End date (yyyy-MM-dd)", defaultValue = ShellOption.NULL) LocalDate toDate) {
        try {
            ListOperationsCommand command;

            if (accountId != null) {
                if (fromDate != null && toDate != null) {
                    command = new ListOperationsCommand(operationFacade, accountId, typeFilter, fromDate, toDate);
                } else {
                    command = new ListOperationsCommand(operationFacade, accountId);
                }
            } else if (fromDate != null && toDate != null) {
                command = new ListOperationsCommand(operationFacade, fromDate, toDate);
            } else if (typeFilter != null) {
                command = new ListOperationsCommand(operationFacade, typeFilter);
            } else {
                command = new ListOperationsCommand(operationFacade);
            }

            List<Operation> operations = commandExecutor.executeCommand(command);

            if (operations.isEmpty()) {
                return "No operations found.";
            }

            String[][] data = new String[operations.size() + 1][6];
            data[0] = new String[]{"ID", "Type", "Account ID", "Amount", "Date", "Description"};

            for (int i = 0; i < operations.size(); i++) {
                Operation op = operations.get(i);
                data[i + 1] = new String[]{
                        op.getId().toString(),
                        op.getType().toString(),
                        op.getBankAccountId().toString(),
                        op.getAmount().toString(),
                        op.getDate().format(DATE_FORMATTER),
                        op.getDescription() != null ? op.getDescription() : ""
                };
            }

            ArrayTableModel model = new ArrayTableModel(data);
            TableBuilder tableBuilder = new TableBuilder(model);
            tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);

            return "Operations:\n" + tableBuilder.build().render(120);
        } catch (Exception e) {
            return "Error listing operations: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get operation details", key = "get-operation")
    public String getOperation(@ShellOption(help = "Operation ID") UUID operationId) {
        try {
            GetOperationDetailsCommand command = new GetOperationDetailsCommand(operationFacade, operationId);
            Optional<OperationFacade.OperationDetails> detailsOpt = commandExecutor.executeCommand(command);

            return detailsOpt.map(details -> details.toString())
                    .orElse("Operation not found with ID: " + operationId);
        } catch (Exception e) {
            return "Error retrieving operation: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Delete an operation", key = "delete-operation")
    public String deleteOperation(@ShellOption(help = "Operation ID") UUID operationId) {
        try {
            DeleteOperationCommand command = new DeleteOperationCommand(operationFacade, operationId);
            boolean success = commandExecutor.executeCommand(command);

            return success
                    ? "Operation deleted successfully"
                    : "Failed to delete operation. It may not exist.";
        } catch (Exception e) {
            return "Error deleting operation: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get income/expense balance for a period", key = "balance")
    public String getBalance(
            @ShellOption(help = "Start date (yyyy-MM-dd)") LocalDate fromDate,
            @ShellOption(help = "End date (yyyy-MM-dd)") LocalDate toDate) {
        try {
            BigDecimal income = operationFacade.getTotalIncome(fromDate, toDate);
            BigDecimal expenses = operationFacade.getTotalExpenses(fromDate, toDate);
            BigDecimal balance = operationFacade.getBalanceDifference(fromDate, toDate);

            return String.format("""
                    Financial Summary (%s - %s):
                    Total Income:  $%.2f
                    Total Expenses: $%.2f
                    ---------------------------
                    Net Balance:   $%.2f
                    """, fromDate, toDate, income, expenses, balance);
        } catch (Exception e) {
            return "Error calculating balance: " + e.getMessage();
        }
    }
}

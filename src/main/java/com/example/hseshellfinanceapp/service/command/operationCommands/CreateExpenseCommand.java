package com.example.hseshellfinanceapp.service.command.operationCommands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class CreateExpenseCommand extends Command<Optional<Operation>> {

    private final OperationFacade operationFacade;
    private final UUID accountId;
    private final UUID categoryId;
    private final BigDecimal amount;
    private final LocalDateTime date;
    private final String description;

    public CreateExpenseCommand(
            OperationFacade operationFacade,
            UUID accountId,
            UUID categoryId,
            BigDecimal amount,
            LocalDateTime date,
            String description) {
        this.operationFacade = operationFacade;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    @Override
    public Optional<Operation> execute() {
        return operationFacade.createExpense(accountId, categoryId, amount, date, description);
    }

    @Override
    protected boolean validate() {
        return accountId != null &&
                categoryId != null &&
                amount != null &&
                amount.compareTo(BigDecimal.ZERO) > 0 &&
                date != null;
    }

    @Override
    public String getHelp() {
        return "CREATE EXPENSE COMMAND\n" +
                "Creates a new expense operation.\n" +
                "Usage: create-expense <account-id> <category-id> <amount> [date] [description]\n" +
                "- account-id: UUID of the account (required)\n" +
                "- category-id: UUID of the expense category (required)\n" +
                "- amount: Expense amount, must be positive (required)\n" +
                "- date: Operation date in ISO format (default: current date/time)\n" +
                "- description: Optional description of the expense";
    }

    @Override
    public String getDescription() {
        return "Create a new expense operation";
    }
}

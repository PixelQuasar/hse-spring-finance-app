package com.example.hseshellfinanceapp.service.command.operationCommands;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class ListOperationsCommand extends Command<List<Operation>> {

    private final OperationFacade operationFacade;
    private final UUID accountId;
    private final OperationType typeFilter;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public ListOperationsCommand(OperationFacade operationFacade) {
        this(operationFacade, null, null, null, null);
    }

    public ListOperationsCommand(OperationFacade operationFacade, UUID accountId) {
        this(operationFacade, accountId, null, null, null);
    }

    public ListOperationsCommand(OperationFacade operationFacade, LocalDate startDate, LocalDate endDate) {
        this(operationFacade, null, null, startDate, endDate);
    }

    public ListOperationsCommand(OperationFacade operationFacade, OperationType typeFilter) {
        this(operationFacade, null, typeFilter, null, null);
    }

    public ListOperationsCommand(
            OperationFacade operationFacade,
            UUID accountId,
            OperationType typeFilter,
            LocalDate startDate,
            LocalDate endDate) {
        this.operationFacade = operationFacade;
        this.accountId = accountId;
        this.typeFilter = typeFilter;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public List<Operation> execute() {
        if (accountId != null) {
            if (startDate != null && endDate != null) {
                return operationFacade.getAccountOperations(accountId, startDate, endDate);
            }
            return operationFacade.getAccountOperations(accountId);
        } else if (startDate != null && endDate != null) {
            return operationFacade.getOperationsByDateRange(startDate, endDate);
        } else if (typeFilter != null) {
            return operationFacade.getOperationsByType(typeFilter);
        } else {
            return operationFacade.getAllOperations();
        }
    }

    @Override
    protected boolean validate() {
        if (startDate != null && endDate != null) {
            return !startDate.isAfter(endDate);
        }
        return true;
    }

    @Override
    public String getHelp() {
        return "LIST OPERATIONS COMMAND\n" +
                "Lists operations with various filtering options.\n" +
                "Usage: list-operations [options]\n" +
                "Options:\n" +
                "  --account <account-id>: Filter by account\n" +
                "  --type <type>: Filter by operation type (INCOME or EXPENSE)\n" +
                "  --from <date>: Start date in ISO format (yyyy-MM-dd)\n" +
                "  --to <date>: End date in ISO format (yyyy-MM-dd)\n" +
                "Examples:\n" +
                "  list-operations --account 123e4567-e89b-12d3-a456-426614174000\n" +
                "  list-operations --from 2023-01-01 --to 2023-01-31\n" +
                "  list-operations --type INCOME";
    }

    @Override
    public String getDescription() {
        return "List financial operations";
    }
}

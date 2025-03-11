package com.example.hseshellfinanceapp.service.command.operationCommands;

import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade.OperationDetails;
import com.example.hseshellfinanceapp.service.command.Command;

public class GetOperationDetailsCommand extends Command<Optional<OperationDetails>> {

    private final OperationFacade operationFacade;
    private final UUID operationId;

    public GetOperationDetailsCommand(OperationFacade operationFacade, UUID operationId) {
        this.operationFacade = operationFacade;
        this.operationId = operationId;
    }

    @Override
    public Optional<OperationDetails> execute() {
        return operationFacade.getOperationDetails(operationId);
    }

    @Override
    protected boolean validate() {
        return operationId != null;
    }

    @Override
    public String getHelp() {
        return "GET OPERATION DETAILS COMMAND\n" +
                "Retrieves detailed information about an operation, including associated account and category.\n" +
                "Usage: get-operation-details <operation-id>\n" +
                "- operation-id: UUID of the operation to retrieve detailed information for (required)";
    }

    @Override
    public String getDescription() {
        return "Get detailed information for a specific operation";
    }
}

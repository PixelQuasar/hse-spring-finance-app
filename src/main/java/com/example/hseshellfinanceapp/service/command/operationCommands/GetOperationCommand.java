package com.example.hseshellfinanceapp.service.command.operationCommands;

import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class GetOperationCommand extends Command<Optional<Operation>> {

    private final OperationFacade operationFacade;
    private final UUID operationId;

    public GetOperationCommand(OperationFacade operationFacade, UUID operationId) {
        this.operationFacade = operationFacade;
        this.operationId = operationId;
    }

    @Override
    public Optional<Operation> execute() {
        return operationFacade.getOperationById(operationId);
    }

    @Override
    protected boolean validate() {
        return operationId != null;
    }

    @Override
    public String getHelp() {
        return "GET OPERATION COMMAND\n" +
                "Retrieves details of a specific operation.\n" +
                "Usage: get-operation <operation-id>\n" +
                "- operation-id: UUID of the operation to retrieve (required)";
    }

    @Override
    public String getDescription() {
        return "Get details for a specific operation";
    }
}

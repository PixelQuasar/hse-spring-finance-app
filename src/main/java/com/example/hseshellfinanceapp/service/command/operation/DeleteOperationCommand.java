package com.example.hseshellfinanceapp.service.command.operation;

import java.util.UUID;

import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class DeleteOperationCommand extends Command<Boolean> {

    private final OperationFacade operationFacade;
    private final UUID operationId;

    public DeleteOperationCommand(OperationFacade operationFacade, UUID operationId) {
        this.operationFacade = operationFacade;
        this.operationId = operationId;
    }

    @Override
    public Boolean execute() {
        return operationFacade.deleteOperation(operationId);
    }

    @Override
    protected boolean validate() {
        return operationId != null;
    }

    @Override
    public String getHelp() {
        return "DELETE OPERATION COMMAND\n" +
                "Deletes an operation and updates the account balance accordingly.\n" +
                "Usage: delete-operation <operation-id>\n" +
                "- operation-id: UUID of the operation to delete (required)";
    }

    @Override
    public String getDescription() {
        return "Delete a financial operation";
    }
}

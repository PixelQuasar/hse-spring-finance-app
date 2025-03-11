package com.example.hseshellfinanceapp.service.command.accountCommands;

import java.util.UUID;

import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class DeleteAccountCommand extends Command<Boolean> {

    private final BankAccountFacade bankAccountFacade;
    private final UUID accountId;

    public DeleteAccountCommand(BankAccountFacade bankAccountFacade, UUID accountId) {
        this.bankAccountFacade = bankAccountFacade;
        this.accountId = accountId;
    }

    @Override
    public Boolean execute() {
        return bankAccountFacade.deleteAccount(accountId);
    }

    @Override
    protected boolean validate() {
        return accountId != null;
    }

    @Override
    public String getHelp() {
        return "DELETE ACCOUNT COMMAND\n" +
                "Deletes a bank account and its associated operations.\n" +
                "Usage: delete-account <account-id>\n" +
                "- account-id: UUID of the account to delete (required)\n" +
                "WARNING: This will also delete all operations associated with the account.";
    }

    @Override
    public String getDescription() {
        return "Delete a bank account";
    }
}

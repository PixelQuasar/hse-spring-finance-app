package com.example.hseshellfinanceapp.service.command.accountCommands;

import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class UpdateAccountCommand extends Command<Optional<BankAccount>> {

    private final BankAccountFacade bankAccountFacade;
    private final UUID accountId;
    private final String newName;

    public UpdateAccountCommand(BankAccountFacade bankAccountFacade, UUID accountId, String newName) {
        this.bankAccountFacade = bankAccountFacade;
        this.accountId = accountId;
        this.newName = newName;
    }

    @Override
    public Optional<BankAccount> execute() {
        return bankAccountFacade.updateAccountName(accountId, newName);
    }

    @Override
    protected boolean validate() {
        return accountId != null &&
                newName != null && !newName.trim().isEmpty();
    }

    @Override
    public String getHelp() {
        return "UPDATE ACCOUNT COMMAND\n" +
                "Updates an existing bank account's name.\n" +
                "Usage: update-account <account-id> <new-name>\n" +
                "- account-id: UUID of the account to update (required)\n" +
                "- new-name: New name for the account (required)";
    }

    @Override
    public String getDescription() {
        return "Update an existing bank account";
    }
}

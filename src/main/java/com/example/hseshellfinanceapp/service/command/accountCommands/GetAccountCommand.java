package com.example.hseshellfinanceapp.service.command.accountCommands;

import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class GetAccountCommand extends Command<Optional<BankAccount>> {

    private final BankAccountFacade bankAccountFacade;
    private final UUID accountId;

    public GetAccountCommand(BankAccountFacade bankAccountFacade, UUID accountId) {
        this.bankAccountFacade = bankAccountFacade;
        this.accountId = accountId;
    }

    @Override
    public Optional<BankAccount> execute() {
        return bankAccountFacade.getAccountById(accountId);
    }

    @Override
    protected boolean validate() {
        return accountId != null;
    }

    @Override
    public String getHelp() {
        return "GET ACCOUNT COMMAND\n" +
                "Retrieves details of a specific bank account.\n" +
                "Usage: get-account <account-id>\n" +
                "- account-id: UUID of the account to retrieve (required)";
    }

    @Override
    public String getDescription() {
        return "Get details for a specific bank account";
    }
}

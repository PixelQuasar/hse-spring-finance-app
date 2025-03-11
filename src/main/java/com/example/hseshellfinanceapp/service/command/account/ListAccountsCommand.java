package com.example.hseshellfinanceapp.service.command.account;

import java.math.BigDecimal;
import java.util.List;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class ListAccountsCommand extends Command<List<BankAccount>> {

    private final BankAccountFacade bankAccountFacade;
    private final BigDecimal minBalance;

    public ListAccountsCommand(BankAccountFacade bankAccountFacade) {
        this(bankAccountFacade, null);
    }

    public ListAccountsCommand(BankAccountFacade bankAccountFacade, BigDecimal minBalance) {
        this.bankAccountFacade = bankAccountFacade;
        this.minBalance = minBalance;
    }

    @Override
    public List<BankAccount> execute() {
        if (minBalance != null) {
            return bankAccountFacade.getAllAccounts().stream()
                    .filter(a -> a.getBalance().compareTo(minBalance) >= 0)
                    .toList();
        }
        return bankAccountFacade.getAllAccounts();
    }

    @Override
    public String getHelp() {
        return "LIST ACCOUNTS COMMAND\n" +
                "Lists all bank accounts.\n" +
                "Usage: list-accounts [min-balance]\n" +
                "- min-balance: Optional minimum balance filter";
    }

    @Override
    public String getDescription() {
        return "List all bank accounts";
    }
}

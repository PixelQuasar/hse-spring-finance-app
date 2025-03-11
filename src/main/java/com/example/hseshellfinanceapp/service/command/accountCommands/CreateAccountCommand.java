package com.example.hseshellfinanceapp.service.command.accountCommands;

import java.math.BigDecimal;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.service.command.Command;

public class CreateAccountCommand extends Command<BankAccount> {

    private final BankAccountFacade bankAccountFacade;
    private final String name;
    private final BigDecimal initialBalance;

    public CreateAccountCommand(BankAccountFacade bankAccountFacade, String name, BigDecimal initialBalance) {
        this.bankAccountFacade = bankAccountFacade;
        this.name = name;
        this.initialBalance = initialBalance;
    }

    public CreateAccountCommand(BankAccountFacade bankAccountFacade, String name) {
        this(bankAccountFacade, name, BigDecimal.ZERO);
    }

    @Override
    public BankAccount execute() {
        return bankAccountFacade.createAccount(name, initialBalance);
    }

    @Override
    protected boolean validate() {
        return name != null && !name.trim().isEmpty() &&
                initialBalance != null && initialBalance.compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public String getHelp() {
        return "CREATE ACCOUNT COMMAND\n" +
                "Creates a new bank account with specified name and initial balance.\n" +
                "Usage: create-account <name> [initial-balance]\n" +
                "- name: Account name (required)\n" +
                "- initial-balance: Initial account balance (default: 0.00)";
    }

    @Override
    public String getDescription() {
        return "Create a new bank account";
    }
}

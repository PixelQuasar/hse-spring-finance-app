package com.example.hseshellfinanceapp.ui.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.service.command.CommandExecutor;
import com.example.hseshellfinanceapp.service.command.accountCommands.CreateAccountCommand;
import com.example.hseshellfinanceapp.service.command.accountCommands.DeleteAccountCommand;
import com.example.hseshellfinanceapp.service.command.accountCommands.GetAccountCommand;
import com.example.hseshellfinanceapp.service.command.accountCommands.ListAccountsCommand;
import com.example.hseshellfinanceapp.service.command.accountCommands.UpdateAccountCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

@ShellComponent
public class AccountHandler {

    private final BankAccountFacade bankAccountFacade;
    private final CommandExecutor commandExecutor;

    @Autowired
    public AccountHandler(BankAccountFacade bankAccountFacade, CommandExecutor commandExecutor) {
        this.bankAccountFacade = bankAccountFacade;
        this.commandExecutor = commandExecutor;
    }

    @ShellMethod(value = "Create a new bank account", key = "create-account")
    public String createAccount(
            @ShellOption(help = "Account name") String name,
            @ShellOption(help = "Initial balance", defaultValue = "0") BigDecimal initialBalance) {
        try {
            CreateAccountCommand command = new CreateAccountCommand(bankAccountFacade, name, initialBalance);
            BankAccount account = commandExecutor.executeCommand(command);
            return "Account created successfully: " + account.toString();
        } catch (Exception e) {
            return "Error creating account: " + e.getMessage();
        }
    }

    @ShellMethod(value = "List all bank accounts", key = "list-accounts")
    public String listAccounts(
            @ShellOption(help = "Minimum balance filter", defaultValue = ShellOption.NULL) BigDecimal minBalance) {
        try {
            ListAccountsCommand command = new ListAccountsCommand(bankAccountFacade, minBalance);
            List<BankAccount> accounts = commandExecutor.executeCommand(command);

            if (accounts.isEmpty()) {
                return "No accounts found.";
            }

            String[][] data = new String[accounts.size() + 1][3];
            data[0] = new String[]{"ID", "Name", "Balance"};

            for (int i = 0; i < accounts.size(); i++) {
                BankAccount account = accounts.get(i);
                data[i + 1] = new String[]{
                        account.getId().toString(),
                        account.getName(),
                        account.getBalance().toString()
                };
            }

            ArrayTableModel model = new ArrayTableModel(data);
            TableBuilder tableBuilder = new TableBuilder(model);
            tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);

            return "Bank Accounts:\n" + tableBuilder.build().render(80);
        } catch (Exception e) {
            return "Error listing accounts: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get account details", key = "get-account")
    public String getAccount(@ShellOption(help = "Account ID") UUID accountId) {
        try {
            GetAccountCommand command = new GetAccountCommand(bankAccountFacade, accountId);
            Optional<BankAccount> accountOpt = commandExecutor.executeCommand(command);

            return accountOpt.map(account -> account.toDetailedString())
                    .orElse("Account not found with ID: " + accountId);
        } catch (Exception e) {
            return "Error retrieving account: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Update account name", key = "update-account")
    public String updateAccount(
            @ShellOption(help = "Account ID") UUID accountId,
            @ShellOption(help = "New account name") String newName) {
        try {
            UpdateAccountCommand command = new UpdateAccountCommand(bankAccountFacade, accountId, newName);
            Optional<BankAccount> accountOpt = commandExecutor.executeCommand(command);

            return accountOpt.map(account -> "Account updated successfully: " + account.toString())
                    .orElse("Account not found with ID: " + accountId);
        } catch (Exception e) {
            return "Error updating account: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Delete an account", key = "delete-account")
    public String deleteAccount(@ShellOption(help = "Account ID") UUID accountId) {
        try {
            DeleteAccountCommand command = new DeleteAccountCommand(bankAccountFacade, accountId);
            boolean success = commandExecutor.executeCommand(command);

            return success
                    ? "Account deleted successfully"
                    : "Failed to delete account. It may not exist or there might be an error.";
        } catch (Exception e) {
            return "Error deleting account: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get total balance across all accounts", key = "total-balance")
    public String getTotalBalance() {
        try {
            BigDecimal totalBalance = bankAccountFacade.getTotalBalance();
            return "Total balance across all accounts: $" + totalBalance;
        } catch (Exception e) {
            return "Error calculating total balance: " + e.getMessage();
        }
    }
}

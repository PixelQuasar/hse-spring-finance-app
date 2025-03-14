package com.example.hseshellfinanceapp.ui.handler;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.service.command.CommandExecutor;
import com.example.hseshellfinanceapp.service.command.accountCommands.CreateAccountCommand;
import com.example.hseshellfinanceapp.service.command.accountCommands.GetAccountCommand;
import com.example.hseshellfinanceapp.service.command.accountCommands.ListAccountsCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountHandlerTest {

    @Mock
    private BankAccountFacade bankAccountFacade;
    @Mock
    private CommandExecutor commandExecutor;

    private AccountHandler accountHandler;

    @BeforeEach
    void setUp() {
        accountHandler = new AccountHandler(bankAccountFacade, commandExecutor);
    }

    @Test
    void createAccount_shouldReturnSuccessMessage() {
        // Given
        String name = "Test Account";
        BigDecimal balance = BigDecimal.TEN;
        BankAccount account = new BankAccount(UUID.randomUUID(), name, balance);

        when(commandExecutor.executeCommand(any(CreateAccountCommand.class))).thenReturn(account);

        // When
        String result = accountHandler.createAccount(name, balance);

        // Then
        assertTrue(result.contains("Account created successfully"));
    }

    @Test
    void listAccounts_withAccounts_shouldDisplayTable() {
        // Given
        BankAccount account = new BankAccount(UUID.randomUUID(), "Test", BigDecimal.TEN);
        List<BankAccount> accounts = Collections.singletonList(account);

        when(commandExecutor.executeCommand(any(ListAccountsCommand.class))).thenReturn(accounts);

        // When
        String result = accountHandler.listAccounts(null);

        // Then
        assertTrue(result.contains("Bank Accounts"));
    }

    @Test
    void getAccount_whenFound_shouldDisplayDetails() {
        // Given
        UUID id = UUID.randomUUID();
        BankAccount account = new BankAccount(id, "Test", BigDecimal.TEN);

        when(commandExecutor.executeCommand(any(GetAccountCommand.class))).thenReturn(Optional.of(account));

        // When
        String result = accountHandler.getAccount(id);

        // Then
        assertTrue(result.contains("ID: " + id));
    }
}

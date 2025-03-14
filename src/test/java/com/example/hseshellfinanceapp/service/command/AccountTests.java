package com.example.hseshellfinanceapp.service.command;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.service.command.accountCommands.CreateAccountCommand;
import com.example.hseshellfinanceapp.service.command.accountCommands.DeleteAccountCommand;
import com.example.hseshellfinanceapp.service.command.accountCommands.GetAccountCommand;
import com.example.hseshellfinanceapp.service.command.accountCommands.ListAccountsCommand;
import com.example.hseshellfinanceapp.service.command.accountCommands.UpdateAccountCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCommandsTest {

    @Mock
    private BankAccountFacade bankAccountFacade;

    // CreateAccountCommand tests
    @Test
    void createAccountCommand_shouldCallFacade() {
        // Given
        String name = "Test Account";
        BigDecimal balance = BigDecimal.TEN;
        CreateAccountCommand command = new CreateAccountCommand(bankAccountFacade, name, balance);
        BankAccount account = new BankAccount(UUID.randomUUID(), name, balance);

        when(bankAccountFacade.createAccount(name, balance)).thenReturn(account);

        // When
        BankAccount result = command.execute();

        // Then
        assertSame(account, result);
        verify(bankAccountFacade).createAccount(name, balance);
    }

    @Test
    void createAccountCommand_withInvalidName_shouldReturnFalseOnValidation() {
        // Given
        CreateAccountCommand command = new CreateAccountCommand(bankAccountFacade, "", BigDecimal.TEN);

        // When
        boolean isValid = command.validate();

        // Then
        assertFalse(isValid);
    }

    // DeleteAccountCommand tests
    @Test
    void deleteAccountCommand_shouldCallFacade() {
        // Given
        UUID id = UUID.randomUUID();
        DeleteAccountCommand command = new DeleteAccountCommand(bankAccountFacade, id);

        when(bankAccountFacade.deleteAccount(id)).thenReturn(true);

        // When
        Boolean result = command.execute();

        // Then
        assertTrue(result);
        verify(bankAccountFacade).deleteAccount(id);
    }

    // GetAccountCommand tests
    @Test
    void getAccountCommand_shouldCallFacade() {
        // Given
        UUID id = UUID.randomUUID();
        GetAccountCommand command = new GetAccountCommand(bankAccountFacade, id);
        Optional<BankAccount> expected = Optional.of(new BankAccount(id, "Test", BigDecimal.TEN));

        when(bankAccountFacade.getAccountById(id)).thenReturn(expected);

        // When
        Optional<BankAccount> result = command.execute();

        // Then
        assertSame(expected, result);
        verify(bankAccountFacade).getAccountById(id);
    }

    // ListAccountsCommand tests
    @Test
    void listAccountsCommand_shouldCallFacade() {
        // Given
        ListAccountsCommand command = new ListAccountsCommand(bankAccountFacade);
        List<BankAccount> expected = List.of(new BankAccount(UUID.randomUUID(), "Test", BigDecimal.TEN));

        when(bankAccountFacade.getAllAccounts()).thenReturn(expected);

        // When
        List<BankAccount> result = command.execute();

        // Then
        assertSame(expected, result);
        verify(bankAccountFacade).getAllAccounts();
    }

    // UpdateAccountCommand tests
    @Test
    void updateAccountCommand_shouldCallFacade() {
        // Given
        UUID id = UUID.randomUUID();
        String newName = "New Name";
        UpdateAccountCommand command = new UpdateAccountCommand(bankAccountFacade, id, newName);
        Optional<BankAccount> expected = Optional.of(new BankAccount(id, newName, BigDecimal.TEN));

        when(bankAccountFacade.updateAccountName(id, newName)).thenReturn(expected);

        // When
        Optional<BankAccount> result = command.execute();

        // Then
        assertSame(expected, result);
        verify(bankAccountFacade).updateAccountName(id, newName);
    }
}

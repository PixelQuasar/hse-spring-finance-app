package com.example.hseshellfinanceapp.facade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.factory.BankAccountFactory;
import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.repository.BankAccountRepository;
import com.example.hseshellfinanceapp.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountFacadeTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private BankAccountFactory bankAccountFactory;

    private BankAccountFacade bankAccountFacade;

    @BeforeEach
    void setUp() {
        bankAccountFacade = new BankAccountFacade(bankAccountRepository, operationRepository, bankAccountFactory);
    }

    @Test
    void createAccount_withNameAndBalance_shouldCreateAndSaveAccount() {
        // Given
        String name = "Test Account";
        BigDecimal initialBalance = new BigDecimal("100.00");
        BankAccount mockAccount = new BankAccount(UUID.randomUUID(), name, initialBalance);

        when(bankAccountFactory.createAccount(name, initialBalance)).thenReturn(mockAccount);
        when(bankAccountRepository.save(mockAccount)).thenReturn(mockAccount);

        // When
        BankAccount result = bankAccountFacade.createAccount(name, initialBalance);

        // Then
        assertEquals(mockAccount, result);
        verify(bankAccountFactory).createAccount(name, initialBalance);
        verify(bankAccountRepository).save(mockAccount);
    }

    @Test
    void createAccount_withNameOnly_shouldCreateAccountWithZeroBalance() {
        // Given
        String name = "Test Account";
        BankAccount mockAccount = new BankAccount(UUID.randomUUID(), name, BigDecimal.ZERO);

        when(bankAccountFactory.createAccount(name, BigDecimal.ZERO)).thenReturn(mockAccount);
        when(bankAccountRepository.save(mockAccount)).thenReturn(mockAccount);

        // When
        BankAccount result = bankAccountFacade.createAccount(name);

        // Then
        assertEquals(mockAccount, result);
        verify(bankAccountFactory).createAccount(name, BigDecimal.ZERO);
        verify(bankAccountRepository).save(mockAccount);
    }

    @Test
    void getAllAccounts_shouldReturnAllAccounts() {
        // Given
        List<BankAccount> mockAccounts = List.of(
                new BankAccount(UUID.randomUUID(), "Account 1", BigDecimal.TEN),
                new BankAccount(UUID.randomUUID(), "Account 2", BigDecimal.ONE)
        );

        when(bankAccountRepository.findAll()).thenReturn(mockAccounts);

        // When
        List<BankAccount> result = bankAccountFacade.getAllAccounts();

        // Then
        assertEquals(mockAccounts, result);
        verify(bankAccountRepository).findAll();
    }

    @Test
    void getAccountById_withExistingId_shouldReturnAccount() {
        // Given
        UUID id = UUID.randomUUID();
        BankAccount mockAccount = new BankAccount(id, "Test Account", BigDecimal.TEN);

        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(mockAccount));

        // When
        Optional<BankAccount> result = bankAccountFacade.getAccountById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(mockAccount, result.get());
        verify(bankAccountRepository).findById(id);
    }

    @Test
    void getAccountById_withNonExistingId_shouldReturnEmpty() {
        // Given
        UUID id = UUID.randomUUID();

        when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<BankAccount> result = bankAccountFacade.getAccountById(id);

        // Then
        assertTrue(result.isEmpty());
        verify(bankAccountRepository).findById(id);
    }

    @Test
    void getAccountByName_withExistingName_shouldReturnAccount() {
        // Given
        String name = "Test Account";
        BankAccount mockAccount = new BankAccount(UUID.randomUUID(), name, BigDecimal.TEN);

        when(bankAccountRepository.findByName(name)).thenReturn(Optional.of(mockAccount));

        // When
        Optional<BankAccount> result = bankAccountFacade.getAccountByName(name);

        // Then
        assertTrue(result.isPresent());
        assertEquals(mockAccount, result.get());
        verify(bankAccountRepository).findByName(name);
    }

    @Test
    void updateAccountName_withExistingId_shouldUpdateAndReturnAccount() {
        // Given
        UUID id = UUID.randomUUID();
        String oldName = "Old Name";
        String newName = "New Name";
        BankAccount mockAccount = new BankAccount(id, oldName, BigDecimal.TEN);
        BankAccount updatedAccount = new BankAccount(id, newName, BigDecimal.TEN);

        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(mockAccount));
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(updatedAccount);

        // When
        Optional<BankAccount> result = bankAccountFacade.updateAccountName(id, newName);

        // Then
        assertTrue(result.isPresent());
        assertEquals(newName, result.get().getName());
        verify(bankAccountRepository).findById(id);
        verify(bankAccountRepository).save(any(BankAccount.class));
    }

    @Test
    void deleteAccount_withExistingId_shouldDeleteAndReturnTrue() {
        // Given
        UUID id = UUID.randomUUID();

        when(bankAccountRepository.existsById(id)).thenReturn(true);
        when(bankAccountRepository.deleteById(id)).thenReturn(true);

        // When
        boolean result = bankAccountFacade.deleteAccount(id);

        // Then
        assertTrue(result);
        verify(bankAccountRepository).existsById(id);
        verify(operationRepository).deleteByBankAccountId(id);
        verify(bankAccountRepository).deleteById(id);
    }

    @Test
    void getTotalBalance_shouldReturnTotalFromRepository() {
        // Given
        BigDecimal expectedTotal = new BigDecimal("150.00");

        when(bankAccountRepository.getTotalBalance()).thenReturn(expectedTotal);

        // When
        BigDecimal result = bankAccountFacade.getTotalBalance();

        // Then
        assertEquals(expectedTotal, result);
        verify(bankAccountRepository).getTotalBalance();
    }

    @Test
    void getAccountOperations_withAccountId_shouldReturnOperationsForAccount() {
        // Given
        UUID accountId = UUID.randomUUID();
        List<Operation> mockOperations = List.of(
                new Operation(UUID.randomUUID(), OperationType.INCOME, accountId,
                        BigDecimal.TEN, LocalDateTime.now(), "Test Income", UUID.randomUUID()),
                new Operation(UUID.randomUUID(), OperationType.EXPENSE, accountId,
                        BigDecimal.ONE, LocalDateTime.now(), "Test Expense", UUID.randomUUID())
        );

        when(operationRepository.findByBankAccountId(accountId)).thenReturn(mockOperations);

        // When
        List<Operation> result = bankAccountFacade.getAccountOperations(accountId);

        // Then
        assertEquals(mockOperations, result);
        verify(operationRepository).findByBankAccountId(accountId);
    }

    @Test
    void getAccountOperations_withAccountIdAndDateRange_shouldReturnFilteredOperations() {
        // Given
        UUID accountId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        UUID differentAccountId = UUID.randomUUID();

        List<Operation> allOperations = List.of(
                new Operation(UUID.randomUUID(), OperationType.INCOME, accountId,
                        BigDecimal.TEN, LocalDateTime.of(2023, 1, 15, 10, 0), "Test Income", UUID.randomUUID()),
                new Operation(UUID.randomUUID(), OperationType.EXPENSE, accountId,
                        BigDecimal.ONE, LocalDateTime.of(2023, 1, 20, 15, 0), "Test Expense", UUID.randomUUID()),
                new Operation(UUID.randomUUID(), OperationType.INCOME, differentAccountId,
                        BigDecimal.valueOf(5), LocalDateTime.of(2023, 1, 10, 12, 0), "Different Account",
                        UUID.randomUUID())
        );

        when(operationRepository.findByDateRange(startDateTime, endDateTime)).thenReturn(allOperations);

        // When
        List<Operation> result = bankAccountFacade.getAccountOperations(accountId, startDate, endDate);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(op -> op.getBankAccountId().equals(accountId)));
        verify(operationRepository).findByDateRange(startDateTime, endDateTime);
    }

    @Test
    void getAccountIncomeOperations_shouldReturnOnlyIncomeOperations() {
        // Given
        UUID accountId = UUID.randomUUID();
        Operation incomeOp = new Operation(UUID.randomUUID(), OperationType.INCOME, accountId,
                BigDecimal.TEN, LocalDateTime.now(), "Test Income", UUID.randomUUID());
        Operation expenseOp = new Operation(UUID.randomUUID(), OperationType.EXPENSE, accountId,
                BigDecimal.ONE, LocalDateTime.now(), "Test Expense", UUID.randomUUID());

        List<Operation> allOperations = List.of(incomeOp, expenseOp);

        when(operationRepository.findByBankAccountId(accountId)).thenReturn(allOperations);

        // When
        List<Operation> result = bankAccountFacade.getAccountIncomeOperations(accountId);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.contains(incomeOp));
        assertFalse(result.contains(expenseOp));
        verify(operationRepository).findByBankAccountId(accountId);
    }

    @Test
    void recalculateBalance_shouldDelegateToRepository() {
        // Given
        UUID accountId = UUID.randomUUID();
        BankAccount mockAccount = new BankAccount(accountId, "Test Account", BigDecimal.TEN);

        when(bankAccountRepository.recalculateBalance(accountId)).thenReturn(Optional.of(mockAccount));

        // When
        Optional<BankAccount> result = bankAccountFacade.recalculateBalance(accountId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(mockAccount, result.get());
        verify(bankAccountRepository).recalculateBalance(accountId);
    }

    @Test
    void getBalanceSummary_withValidInput_shouldReturnBalanceSummary() {
        // Given
        UUID accountId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        BigDecimal totalIncome = new BigDecimal("150.00");
        BigDecimal totalExpenses = new BigDecimal("50.00");
        BigDecimal netChange = new BigDecimal("100.00");

        when(bankAccountRepository.existsById(accountId)).thenReturn(true);
        when(operationRepository.sumByDateRangeAndType(startDateTime, endDateTime, OperationType.INCOME))
                .thenReturn(totalIncome);
        when(operationRepository.sumByDateRangeAndType(startDateTime, endDateTime, OperationType.EXPENSE))
                .thenReturn(totalExpenses);

        // When
        BankAccountFacade.AccountBalanceSummary result =
                bankAccountFacade.getBalanceSummary(accountId, startDate, endDate);

        // Then
        assertEquals(totalIncome, result.getTotalIncome());
        assertEquals(totalExpenses, result.getTotalExpenses());
        assertEquals(netChange, result.getNetChange());
        verify(bankAccountRepository).existsById(accountId);
        verify(operationRepository).sumByDateRangeAndType(startDateTime, endDateTime, OperationType.INCOME);
        verify(operationRepository).sumByDateRangeAndType(startDateTime, endDateTime, OperationType.EXPENSE);
    }

    @Test
    void getBalanceSummary_withNonExistingAccount_shouldThrowException() {
        // Given
        UUID accountId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(bankAccountRepository.existsById(accountId)).thenReturn(false);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bankAccountFacade.getBalanceSummary(accountId, startDate, endDate)
        );

        assertEquals("Account not found: " + accountId, exception.getMessage());
        verify(bankAccountRepository).existsById(accountId);
        verifyNoInteractions(operationRepository);
    }

    @Test
    void accountBalanceSummary_toString_shouldReturnFormattedString() {
        // Given
        BigDecimal income = new BigDecimal("150.00");
        BigDecimal expenses = new BigDecimal("50.00");
        BigDecimal netChange = new BigDecimal("100.00");
        BankAccountFacade.AccountBalanceSummary summary =
                new BankAccountFacade.AccountBalanceSummary(income, expenses, netChange);

        // When
        String result = summary.toString();

        // Then
        String expected = "Total Income: $150.00, Total Expenses: $50.00, Net Change: $100.00";
        assertEquals(expected, result);
    }
}

package com.example.hseshellfinanceapp.facade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.factory.OperationFactory;
import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.repository.BankAccountRepository;
import com.example.hseshellfinanceapp.repository.CategoryRepository;
import com.example.hseshellfinanceapp.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationFacadeTest {

    @Mock
    private OperationRepository operationRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private OperationFactory operationFactory;

    private OperationFacade operationFacade;

    @BeforeEach
    void setUp() {
        operationFacade = new OperationFacade(operationRepository, bankAccountRepository, categoryRepository,
                operationFactory);
    }

    @Test
    void createIncome_withValidData_shouldCreateAndSaveOperation() {
        // Given
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");
        LocalDateTime date = LocalDateTime.now();
        String description = "Test income";

        BankAccount account = new BankAccount(accountId, "Test Account", BigDecimal.ZERO);
        Category category = new Category(categoryId, "Salary", OperationType.INCOME);
        Operation operation = new Operation(UUID.randomUUID(), OperationType.INCOME, accountId, amount, date,
                description, categoryId);

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(operationFactory.createIncome(accountId, amount, date, description, categoryId)).thenReturn(operation);
        when(operationRepository.save(operation)).thenReturn(operation);

        // When
        Optional<Operation> result = operationFacade.createIncome(accountId, categoryId, amount, date, description);

        // Then
        assertTrue(result.isPresent());
        assertEquals(operation, result.get());
        verify(bankAccountRepository).updateBalance(accountId, amount);
    }

    @Test
    void createExpense_withInsufficientFunds_shouldThrowException() {
        // Given
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");
        LocalDateTime date = LocalDateTime.now();
        String description = "Test expense";

        BankAccount account = new BankAccount(accountId, "Test Account", new BigDecimal("50.00"));
        Category category = new Category(categoryId, "Shopping", OperationType.EXPENSE);

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> operationFacade.createExpense(accountId, categoryId, amount, date, description)
        );

        assertEquals("Insufficient funds in account", exception.getMessage());
    }

    @Test
    void deleteOperation_withIncomeOperation_shouldUpdateBalanceCorrectly() {
        // Given
        UUID operationId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");

        Operation operation = new Operation(operationId, OperationType.INCOME, accountId,
                amount, LocalDateTime.now(), "Test income", UUID.randomUUID());

        when(operationRepository.findById(operationId)).thenReturn(Optional.of(operation));
        when(operationRepository.deleteById(operationId)).thenReturn(true);

        // When
        boolean result = operationFacade.deleteOperation(operationId);

        // Then
        assertTrue(result);
        verify(bankAccountRepository).updateBalance(accountId, amount.negate());
    }

    @Test
    void getOperationDetails_withValidId_shouldReturnDetails() {
        // Given
        UUID operationId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Operation operation = new Operation(operationId, OperationType.EXPENSE, accountId,
                BigDecimal.TEN, LocalDateTime.now(), "Test expense", categoryId);
        BankAccount account = new BankAccount(accountId, "Test Account", BigDecimal.ZERO);
        Category category = new Category(categoryId, "Shopping", OperationType.EXPENSE);

        when(operationRepository.findById(operationId)).thenReturn(Optional.of(operation));
        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        Optional<OperationFacade.OperationDetails> result = operationFacade.getOperationDetails(operationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(operation, result.get().getOperation());
        assertEquals("Test Account", result.get().getAccountName());
        assertEquals("Shopping", result.get().getCategoryName());
    }
}

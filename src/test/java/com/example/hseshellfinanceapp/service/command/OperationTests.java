package com.example.hseshellfinanceapp.service.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade.OperationDetails;
import com.example.hseshellfinanceapp.service.command.operationCommands.CreateExpenseCommand;
import com.example.hseshellfinanceapp.service.command.operationCommands.CreateIncomeCommand;
import com.example.hseshellfinanceapp.service.command.operationCommands.DeleteOperationCommand;
import com.example.hseshellfinanceapp.service.command.operationCommands.GetOperationCommand;
import com.example.hseshellfinanceapp.service.command.operationCommands.GetOperationDetailsCommand;
import com.example.hseshellfinanceapp.service.command.operationCommands.ListOperationsCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationCommandsTest {

    @Mock
    private OperationFacade operationFacade;

    // CreateExpenseCommand tests
    @Test
    void createExpenseCommand_shouldCallFacade() {
        // Given
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.TEN;
        LocalDateTime date = LocalDateTime.now();
        String description = "Test expense";
        CreateExpenseCommand command = new CreateExpenseCommand(
                operationFacade, accountId, categoryId, amount, date, description);

        Operation operation = new Operation(UUID.randomUUID(), OperationType.EXPENSE,
                accountId, amount, date, description, categoryId);

        when(operationFacade.createExpense(accountId, categoryId, amount, date, description))
                .thenReturn(Optional.of(operation));

        // When
        Optional<Operation> result = command.execute();

        // Then
        assertTrue(result.isPresent());
        assertSame(operation, result.get());
        verify(operationFacade).createExpense(accountId, categoryId, amount, date, description);
    }

    // CreateIncomeCommand tests
    @Test
    void createIncomeCommand_shouldCallFacade() {
        // Given
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.TEN;
        LocalDateTime date = LocalDateTime.now();
        String description = "Test income";
        CreateIncomeCommand command = new CreateIncomeCommand(
                operationFacade, accountId, categoryId, amount, date, description);

        Operation operation = new Operation(UUID.randomUUID(), OperationType.INCOME,
                accountId, amount, date, description, categoryId);

        when(operationFacade.createIncome(accountId, categoryId, amount, date, description))
                .thenReturn(Optional.of(operation));

        // When
        Optional<Operation> result = command.execute();

        // Then
        assertTrue(result.isPresent());
        assertSame(operation, result.get());
        verify(operationFacade).createIncome(accountId, categoryId, amount, date, description);
    }

    // DeleteOperationCommand tests
    @Test
    void deleteOperationCommand_shouldCallFacade() {
        // Given
        UUID operationId = UUID.randomUUID();
        DeleteOperationCommand command = new DeleteOperationCommand(operationFacade, operationId);

        when(operationFacade.deleteOperation(operationId)).thenReturn(true);

        // When
        Boolean result = command.execute();

        // Then
        assertTrue(result);
        verify(operationFacade).deleteOperation(operationId);
    }

    // GetOperationCommand tests
    @Test
    void getOperationCommand_shouldCallFacade() {
        // Given
        UUID operationId = UUID.randomUUID();
        GetOperationCommand command = new GetOperationCommand(operationFacade, operationId);

        Operation operation = new Operation(operationId, OperationType.EXPENSE,
                UUID.randomUUID(), BigDecimal.TEN, LocalDateTime.now(), "Test", UUID.randomUUID());

        when(operationFacade.getOperationById(operationId)).thenReturn(Optional.of(operation));

        // When
        Optional<Operation> result = command.execute();

        // Then
        assertTrue(result.isPresent());
        assertSame(operation, result.get());
        verify(operationFacade).getOperationById(operationId);
    }

    // GetOperationDetailsCommand tests
    @Test
    void getOperationDetailsCommand_shouldCallFacade() {
        // Given
        UUID operationId = UUID.randomUUID();
        GetOperationDetailsCommand command = new GetOperationDetailsCommand(operationFacade, operationId);

        Operation operation = new Operation(operationId, OperationType.EXPENSE,
                UUID.randomUUID(), BigDecimal.TEN, LocalDateTime.now(), "Test", UUID.randomUUID());
        OperationDetails details = new OperationDetails(operation, "Test Account", "Test Category");

        when(operationFacade.getOperationDetails(operationId)).thenReturn(Optional.of(details));

        // When
        Optional<OperationDetails> result = command.execute();

        // Then
        assertTrue(result.isPresent());
        assertSame(details, result.get());
        verify(operationFacade).getOperationDetails(operationId);
    }

    // ListOperationsCommand tests
    @Test
    void listOperationsCommand_shouldCallFacade() {
        // Given
        ListOperationsCommand command = new ListOperationsCommand(operationFacade);

        List<Operation> operations = List.of(
                new Operation(UUID.randomUUID(), OperationType.EXPENSE,
                        UUID.randomUUID(), BigDecimal.TEN, LocalDateTime.now(), "Test", UUID.randomUUID())
        );

        when(operationFacade.getAllOperations()).thenReturn(operations);

        // When
        List<Operation> result = command.execute();

        // Then
        assertSame(operations, result);
        verify(operationFacade).getAllOperations();
    }
}

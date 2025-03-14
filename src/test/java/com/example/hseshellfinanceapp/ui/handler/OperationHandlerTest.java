package com.example.hseshellfinanceapp.ui.handler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.example.hseshellfinanceapp.service.command.CommandExecutor;
import com.example.hseshellfinanceapp.service.command.operationCommands.CreateIncomeCommand;
import com.example.hseshellfinanceapp.service.command.operationCommands.ListOperationsCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationHandlerTest {

    @Mock
    private OperationFacade operationFacade;
    @Mock
    private CommandExecutor commandExecutor;

    private OperationHandler operationHandler;

    @BeforeEach
    void setUp() {
        operationHandler = new OperationHandler(operationFacade, commandExecutor);
    }

    @Test
    void createIncome_shouldReturnSuccessMessage() {
        // Given
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.TEN;
        String description = "Test income";

        Operation operation = new Operation(UUID.randomUUID(), OperationType.INCOME,
                accountId, amount, LocalDateTime.now(), description, categoryId);

        when(commandExecutor.executeCommand(any(CreateIncomeCommand.class)))
                .thenReturn(Optional.of(operation));

        // When
        String result = operationHandler.createIncome(accountId, categoryId, amount, description);

        // Then
        assertTrue(result.contains("Income added successfully"));
    }

    @Test
    void listOperations_withOperations_shouldDisplayTable() {
        // Given
        Operation operation = new Operation(
                UUID.randomUUID(), OperationType.EXPENSE, UUID.randomUUID(),
                BigDecimal.TEN, LocalDateTime.now(), "Test", UUID.randomUUID());
        List<Operation> operations = Collections.singletonList(operation);

        when(commandExecutor.executeCommand(any(ListOperationsCommand.class))).thenReturn(operations);

        // When
        String result = operationHandler.listOperations(null, null, null, null);

        // Then
        assertTrue(result.contains("Operations:"));
    }
}

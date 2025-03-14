package com.example.hseshellfinanceapp.ui.handler;

import java.time.LocalDate;
import java.util.UUID;

import com.example.hseshellfinanceapp.service.analytics.AnalyticsService;
import com.example.hseshellfinanceapp.service.analytics.ReportGenerator;
import com.example.hseshellfinanceapp.service.command.CommandExecutor;
import com.example.hseshellfinanceapp.service.command.analyticsCommands.AccountSummaryCommand;
import com.example.hseshellfinanceapp.service.command.analyticsCommands.ExpensesByCategoryCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsHandlerTest {

    @Mock
    private AnalyticsService analyticsService;
    @Mock
    private ReportGenerator reportGenerator;
    @Mock
    private CommandExecutor commandExecutor;

    private AnalyticsHandler analyticsHandler;

    @BeforeEach
    void setUp() {
        analyticsHandler = new AnalyticsHandler(analyticsService, reportGenerator, commandExecutor);
    }

    @Test
    void getExpensesByCategory_shouldReturnCommandResult() {
        // Given
        Integer month = 1;
        Integer year = 2023;
        String expectedReport = "Expenses Report";

        when(commandExecutor.executeCommand(any(ExpensesByCategoryCommand.class))).thenReturn(expectedReport);

        // When
        String result = analyticsHandler.getExpensesByCategory(month, year);

        // Then
        assertEquals(expectedReport, result);
    }

    @Test
    void getAccountSummary_shouldReturnCommandResult() {
        // Given
        UUID accountId = UUID.randomUUID();
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 1, 31);
        String expectedReport = "Account Summary Report";

        when(commandExecutor.executeCommand(any(AccountSummaryCommand.class))).thenReturn(expectedReport);

        // When
        String result = analyticsHandler.getAccountSummary(accountId, fromDate, toDate);

        // Then
        assertEquals(expectedReport, result);
    }
}

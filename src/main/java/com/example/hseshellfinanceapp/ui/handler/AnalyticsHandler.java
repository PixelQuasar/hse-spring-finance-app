package com.example.hseshellfinanceapp.ui.handler;

import java.time.LocalDate;
import java.util.UUID;

import com.example.hseshellfinanceapp.service.analytics.AnalyticsService;
import com.example.hseshellfinanceapp.service.analytics.ReportGenerator;
import com.example.hseshellfinanceapp.service.command.CommandExecutor;
import com.example.hseshellfinanceapp.service.command.analyticsCommands.AccountSummaryCommand;
import com.example.hseshellfinanceapp.service.command.analyticsCommands.ExpensesByCategoryCommand;
import com.example.hseshellfinanceapp.service.command.analyticsCommands.IncomesByCategoryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class AnalyticsHandler {

    private final AnalyticsService analyticsService;
    private final ReportGenerator reportGenerator;
    private final CommandExecutor commandExecutor;

    @Autowired
    public AnalyticsHandler(
            AnalyticsService analyticsService,
            ReportGenerator reportGenerator,
            CommandExecutor commandExecutor) {
        this.analyticsService = analyticsService;
        this.reportGenerator = reportGenerator;
        this.commandExecutor = commandExecutor;
    }

    @ShellMethod(value = "Get expenses by category", key = "expenses-by-category")
    public String getExpensesByCategory(
            @ShellOption(help = "Month (1-12)", defaultValue = ShellOption.NULL) Integer month,
            @ShellOption(help = "Year", defaultValue = ShellOption.NULL) Integer year) {
        try {
            ExpensesByCategoryCommand command = new ExpensesByCategoryCommand(
                    analyticsService, reportGenerator, month, year);
            return commandExecutor.executeCommand(command);
        } catch (Exception e) {
            return "Error analyzing expenses: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get income by category", key = "income-by-category")
    public String getIncomeByCategory(
            @ShellOption(help = "Month (1-12)", defaultValue = ShellOption.NULL) Integer month,
            @ShellOption(help = "Year", defaultValue = ShellOption.NULL) Integer year) {
        try {
            IncomesByCategoryCommand command = new IncomesByCategoryCommand(
                    analyticsService, reportGenerator, month, year);
            return commandExecutor.executeCommand(command);
        } catch (Exception e) {
            return "Error analyzing income: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Get account balance summary", key = "account-summary")
    public String getAccountSummary(
            @ShellOption(help = "Account ID") UUID accountId,
            @ShellOption(help = "Start date (yyyy-MM-dd)") LocalDate fromDate,
            @ShellOption(help = "End date (yyyy-MM-dd)") LocalDate toDate) {
        try {
            AccountSummaryCommand command = new AccountSummaryCommand(
                    analyticsService, reportGenerator, accountId, fromDate, toDate);
            return commandExecutor.executeCommand(command);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error generating account summary: " + e.getMessage();
        }
    }
}

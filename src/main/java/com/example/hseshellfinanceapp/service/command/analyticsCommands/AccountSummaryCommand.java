package com.example.hseshellfinanceapp.service.command.analyticsCommands;

import java.time.LocalDate;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.facade.BankAccountFacade.AccountBalanceSummary;
import com.example.hseshellfinanceapp.service.analytics.AnalyticsService;
import com.example.hseshellfinanceapp.service.analytics.ReportGenerator;
import com.example.hseshellfinanceapp.service.command.Command;

public class AccountSummaryCommand extends Command<String> {

    private final AnalyticsService analyticsService;
    private final ReportGenerator reportGenerator;
    private final UUID accountId;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public AccountSummaryCommand(
            AnalyticsService analyticsService,
            ReportGenerator reportGenerator,
            UUID accountId,
            LocalDate fromDate,
            LocalDate toDate) {
        this.analyticsService = analyticsService;
        this.reportGenerator = reportGenerator;
        this.accountId = accountId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public String execute() {
        // Get account and summary data from analytics service
        BankAccount account = analyticsService.getAccountById(accountId);
        AccountBalanceSummary summary = analyticsService.getAccountBalanceSummary(accountId, fromDate, toDate);

        // Use report generator to format output
        return reportGenerator.generateAccountSummary(account, summary, fromDate, toDate);
    }

    @Override
    protected boolean validate() {
        if (accountId == null) {
            return false;
        }
        if (fromDate == null || toDate == null) {
            return false;
        }
        if (fromDate.isAfter(toDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String getHelp() {
        return "ACCOUNT SUMMARY COMMAND\n" +
                "Shows a summary of account activity for a specific period.\n" +
                "Usage: account-summary --account-id <uuid> --from-date <date> --to-date <date>\n" +
                "Options:\n" +
                "  --account-id: UUID of the account to analyze\n" +
                "  --from-date: Start date in format yyyy-MM-dd\n" +
                "  --to-date: End date in format yyyy-MM-dd\n" +
                "Example: account-summary --account-id 12345678-1234-1234-1234-123456789012 --from-date 2023-01-01 " +
                "--to-date 2023-03-31";
    }

    @Override
    public String getDescription() {
        return "Get account balance summary";
    }
}

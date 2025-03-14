package com.example.hseshellfinanceapp.service.command.analyticsCommands;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;

import com.example.hseshellfinanceapp.service.analytics.AnalyticsService;
import com.example.hseshellfinanceapp.service.analytics.ReportGenerator;
import com.example.hseshellfinanceapp.service.command.Command;

public class IncomesByCategoryCommand extends Command<String> {

    private final AnalyticsService analyticsService;
    private final ReportGenerator reportGenerator;
    private final Integer month;
    private final Integer year;

    public IncomesByCategoryCommand(
            AnalyticsService analyticsService,
            ReportGenerator reportGenerator,
            Integer month,
            Integer year) {
        this.analyticsService = analyticsService;
        this.reportGenerator = reportGenerator;
        this.month = month;
        this.year = year;
    }

    @Override
    public String execute() {
        // Set default period to current month if not specified
        YearMonth period = null;
        if (month != null && year != null) {
            period = YearMonth.of(year, month);
        }

        // Get data from analytics service
        Map<String, BigDecimal> incomesByCategory = analyticsService.getIncomesByCategory(period);

        if (incomesByCategory.isEmpty()) {
            return "No income found for the period.";
        }

        // Use report generator to format output
        String title = period == null
                ? "Income by Category (Current Month)"
                : String.format("Income by Category (%d-%02d)", year, month);

        return reportGenerator.generateCategoryReport(incomesByCategory, title);
    }

    @Override
    protected boolean validate() {
        if (month != null && (month < 1 || month > 12)) {
            return false;
        }
        if (month != null && year == null) {
            return false;
        }
        return true;
    }

    @Override
    public String getHelp() {
        return "INCOME BY CATEGORY COMMAND\n" +
                "Shows income grouped by category for a specific period.\n" +
                "Usage: income-by-category [--month <month>] [--year <year>]\n" +
                "Options:\n" +
                "  --month: Month number (1-12)\n" +
                "  --year: Year (e.g., 2023)\n" +
                "If month and year are not specified, current month is used.";
    }

    @Override
    public String getDescription() {
        return "Get income by category";
    }
}

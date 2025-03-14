package com.example.hseshellfinanceapp.service.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.facade.BankAccountFacade.AccountBalanceSummary;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.stereotype.Component;

@Component
public class ReportGenerator {

    public String generateCategoryReport(Map<String, BigDecimal> categoryData, String title) {
        if (categoryData == null || categoryData.isEmpty()) {
            return "No data available for the report.";
        }

        List<Map.Entry<String, BigDecimal>> sortedEntries = new ArrayList<>(categoryData.entrySet());
        sortedEntries.sort(Map.Entry.<String, BigDecimal>comparingByValue().reversed());

        String[][] data = new String[sortedEntries.size() + 2][2]; // +2 for header and total row
        data[0] = new String[]{"Category", "Amount"};

        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < sortedEntries.size(); i++) {
            Map.Entry<String, BigDecimal> entry = sortedEntries.get(i);
            data[i + 1] = new String[]{entry.getKey(), formatCurrency(entry.getValue())};
            total = total.add(entry.getValue());
        }

        data[sortedEntries.size() + 1] = new String[]{"TOTAL", formatCurrency(total)};

        ArrayTableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);

        return title + "\n" + tableBuilder.build().render(80);
    }

    public String generateAccountSummary(BankAccount account, AccountBalanceSummary summary,
                                         LocalDate fromDate, LocalDate toDate) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("ACCOUNT SUMMARY: %s\n", account.getName()));
        result.append(String.format("Period: %s to %s\n\n", fromDate, toDate));

        String[][] data = new String[4][2];
        data[0] = new String[]{"Metric", "Amount"};
        data[1] = new String[]{"Total Income", formatCurrency(summary.getTotalIncome())};
        data[2] = new String[]{"Total Expenses", formatCurrency(summary.getTotalExpenses())};
        data[3] = new String[]{"Net Change", formatCurrency(summary.getNetChange())};

        ArrayTableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);

        result.append(tableBuilder.build().render(60)).append("\n\n");
        result.append(String.format("Current Account Balance: %s\n", formatCurrency(account.getBalance())));

        return result.toString();
    }

    public String generateTrendReport(Map<String, BigDecimal> trendData, String title) {
        if (trendData == null || trendData.isEmpty()) {
            return "No trend data available.";
        }

        String[][] data = new String[trendData.size() + 1][2];
        data[0] = new String[]{"Month", "Amount"};

        int i = 1;
        for (Map.Entry<String, BigDecimal> entry : trendData.entrySet()) {
            data[i++] = new String[]{entry.getKey(), formatCurrency(entry.getValue())};
        }

        ArrayTableModel model = new ArrayTableModel(data);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);

        return title + "\n" + tableBuilder.build().render(80);
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }
        return String.format("$%.2f", amount);
    }
}

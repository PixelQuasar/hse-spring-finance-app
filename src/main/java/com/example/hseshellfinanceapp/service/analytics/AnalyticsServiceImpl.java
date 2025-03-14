package com.example.hseshellfinanceapp.service.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.BankAccountFacade.AccountBalanceSummary;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final BankAccountFacade bankAccountFacade;
    private final CategoryFacade categoryFacade;
    private final OperationFacade operationFacade;

    @Autowired
    public AnalyticsServiceImpl(
            BankAccountFacade bankAccountFacade,
            CategoryFacade categoryFacade,
            OperationFacade operationFacade) {
        this.bankAccountFacade = bankAccountFacade;
        this.categoryFacade = categoryFacade;
        this.operationFacade = operationFacade;
    }

    @Override
    public Map<String, BigDecimal> getExpensesByCategory(YearMonth period) {
        YearMonth targetPeriod = period != null ? period : YearMonth.now();

        LocalDate startDate = targetPeriod.atDay(1);
        LocalDate endDate = targetPeriod.atEndOfMonth();

        return categoryFacade.getSpendingByCategory();
    }

    @Override
    public Map<String, BigDecimal> getIncomesByCategory(YearMonth period) {
        YearMonth targetPeriod = period != null ? period : YearMonth.now();

        LocalDate startDate = targetPeriod.atDay(1);
        LocalDate endDate = targetPeriod.atEndOfMonth();

        return categoryFacade.getIncomeByCategory();
    }

    @Override
    public AccountBalanceSummary getAccountBalanceSummary(UUID accountId, LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return bankAccountFacade.getBalanceSummary(accountId, fromDate, toDate);
    }

    @Override
    public Map<String, BigDecimal> getMonthlyTrend(int months, OperationType type) {
        YearMonth currentMonth = YearMonth.now();
        Map<String, BigDecimal> trend = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

        for (int i = months - 1; i >= 0; i--) {
            YearMonth targetMonth = currentMonth.minusMonths(i);
            LocalDate startDate = targetMonth.atDay(1);
            LocalDate endDate = targetMonth.atEndOfMonth();

            BigDecimal amount;
            if (type == OperationType.INCOME) {
                amount = operationFacade.getTotalIncome(startDate, endDate);
            } else if (type == OperationType.EXPENSE) {
                amount = operationFacade.getTotalExpenses(startDate, endDate);
            } else {
                // Net change (income - expenses)
                amount = operationFacade.getBalanceDifference(startDate, endDate);
            }

            if (amount == null) {
                amount = BigDecimal.ZERO;
            }

            trend.put(targetMonth.format(formatter), amount);
        }

        return trend;
    }

    @Override
    public Map<String, BigDecimal> getTopSpendingCategories(YearMonth period, int limit) {
        Map<String, BigDecimal> allExpenses = getExpensesByCategory(period);

        return allExpenses.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public BankAccount getAccountById(UUID accountId) {
        return bankAccountFacade.getAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
    }
}

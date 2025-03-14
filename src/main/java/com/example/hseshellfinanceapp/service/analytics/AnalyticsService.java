package com.example.hseshellfinanceapp.service.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.facade.BankAccountFacade.AccountBalanceSummary;

public interface AnalyticsService {
    Map<String, BigDecimal> getExpensesByCategory(YearMonth period);

    Map<String, BigDecimal> getIncomesByCategory(YearMonth period);

    AccountBalanceSummary getAccountBalanceSummary(UUID accountId, LocalDate fromDate, LocalDate toDate);

    Map<String, BigDecimal> getMonthlyTrend(int months, OperationType type);

    Map<String, BigDecimal> getTopSpendingCategories(YearMonth period, int limit);

    BankAccount getAccountById(UUID accountId);
}

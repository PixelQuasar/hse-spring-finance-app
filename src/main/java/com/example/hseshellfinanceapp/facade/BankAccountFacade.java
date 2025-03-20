package com.example.hseshellfinanceapp.facade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.factory.BankAccountFactory;
import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.repository.BankAccountRepository;
import com.example.hseshellfinanceapp.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class BankAccountFacade {

    private final BankAccountRepository bankAccountRepository;
    private final OperationRepository operationRepository;
    private final BankAccountFactory bankAccountFactory;

    @Autowired
    public BankAccountFacade(
            BankAccountRepository bankAccountRepository,
            OperationRepository operationRepository,
            BankAccountFactory bankAccountFactory) {
        this.bankAccountRepository = bankAccountRepository;
        this.operationRepository = operationRepository;
        this.bankAccountFactory = bankAccountFactory;
    }

    public BankAccount createAccount(String name, BigDecimal initialBalance) {
        BankAccount account = bankAccountFactory.createAccount(name, initialBalance);
        return bankAccountRepository.save(account);
    }

    public BankAccount createAccount(String name) {
        return createAccount(name, BigDecimal.ZERO);
    }

    public List<BankAccount> getAllAccounts() {
        return bankAccountRepository.findAll();
    }

    public Optional<BankAccount> getAccountById(UUID id) {
        return bankAccountRepository.findById(id);
    }

    public Optional<BankAccount> getAccountByName(String name) {
        return bankAccountRepository.findByName(name);
    }

    public Optional<BankAccount> updateAccountName(UUID id, String newName) {
        Optional<BankAccount> accountOpt = bankAccountRepository.findById(id);
        if (accountOpt.isEmpty()) {
            return Optional.empty();
        }

        BankAccount account = accountOpt.get();
        account.setName(newName);
        return Optional.of(bankAccountRepository.save(account));
    }

    @Transactional
    public boolean deleteAccount(UUID id) {
        if (!bankAccountRepository.existsById(id)) {
            return false;
        }

        operationRepository.deleteByBankAccountId(id);

        return bankAccountRepository.deleteById(id);
    }

    public BigDecimal getTotalBalance() {
        return bankAccountRepository.getTotalBalance();
    }

    public List<Operation> getAccountOperations(UUID accountId) {
        return operationRepository.findByBankAccountId(accountId);
    }

    public List<Operation> getAccountOperations(UUID accountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        return operationRepository.findByDateRange(startDateTime, endDateTime)
                .stream()
                .filter(op -> op.getBankAccountId().equals(accountId))
                .toList();
    }

    public List<Operation> getAccountIncomeOperations(UUID accountId) {
        return operationRepository.findByBankAccountId(accountId)
                .stream()
                .filter(Operation::isIncome)
                .toList();
    }

    @Transactional
    public Optional<BankAccount> recalculateBalance(UUID accountId) {
        return bankAccountRepository.recalculateBalance(accountId);
    }

    public AccountBalanceSummary getBalanceSummary(UUID accountId, LocalDate startDate, LocalDate endDate) {
        if (!bankAccountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        BigDecimal totalIncome = operationRepository.sumByDateRangeAndType(
                startDateTime, endDateTime, OperationType.INCOME);

        BigDecimal totalExpenses = operationRepository.sumByDateRangeAndType(
                startDateTime, endDateTime, OperationType.EXPENSE);

        if (totalIncome == null) {
            totalIncome = BigDecimal.ZERO;
        }
        if (totalExpenses == null) {
            totalExpenses = BigDecimal.ZERO;
        }

        BigDecimal netChange = totalIncome.subtract(totalExpenses);

        return new AccountBalanceSummary(totalIncome, totalExpenses, netChange);
    }

    public static class AccountBalanceSummary {
        private final BigDecimal totalIncome;
        private final BigDecimal totalExpenses;
        private final BigDecimal netChange;

        public AccountBalanceSummary(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal netChange) {
            this.totalIncome = totalIncome;
            this.totalExpenses = totalExpenses;
            this.netChange = netChange;
        }

        public BigDecimal getTotalIncome() {
            return totalIncome;
        }

        public BigDecimal getTotalExpenses() {
            return totalExpenses;
        }

        public BigDecimal getNetChange() {
            return netChange;
        }

        @Override
        public String toString() {
            return String.format("Total Income: $%.2f, Total Expenses: $%.2f, Net Change: $%.2f",
                    totalIncome, totalExpenses, netChange);
        }
    }
}

package com.example.hseshellfinanceapp.facade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.factory.OperationFactory;
import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.Category;
import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import com.example.hseshellfinanceapp.repository.BankAccountRepository;
import com.example.hseshellfinanceapp.repository.CategoryRepository;
import com.example.hseshellfinanceapp.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OperationFacade {

    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;
    private final OperationFactory operationFactory;

    @Autowired
    public OperationFacade(
            OperationRepository operationRepository,
            BankAccountRepository bankAccountRepository,
            CategoryRepository categoryRepository,
            OperationFactory operationFactory) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
        this.operationFactory = operationFactory;
    }

    @Transactional
    public Optional<Operation> createIncome(
            UUID accountId,
            UUID categoryId,
            BigDecimal amount,
            LocalDateTime date,
            String description) {

        Optional<BankAccount> accountOpt = bankAccountRepository.findById(accountId);
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (accountOpt.isEmpty() || categoryOpt.isEmpty()) {
            return Optional.empty();
        }

        Category category = categoryOpt.get();
        if (category.getType() != OperationType.INCOME) {
            throw new IllegalArgumentException("Category is not an income category");
        }

        Operation operation = operationFactory.createIncome(
                accountId, amount, date, description, categoryId);

        operation = operationRepository.save(operation);

        bankAccountRepository.updateBalance(accountId, amount);

        return Optional.of(operation);
    }

    @Transactional
    public Optional<Operation> createExpense(
            UUID accountId,
            UUID categoryId,
            BigDecimal amount,
            LocalDateTime date,
            String description) {

        Optional<BankAccount> accountOpt = bankAccountRepository.findById(accountId);
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (accountOpt.isEmpty() || categoryOpt.isEmpty()) {
            return Optional.empty();
        }

        BankAccount account = accountOpt.get();
        Category category = categoryOpt.get();

        if (category.getType() != OperationType.EXPENSE) {
            throw new IllegalArgumentException("Category is not an expense category");
        }

        if (!account.hasSufficientFunds(amount)) {
            throw new IllegalArgumentException("Insufficient funds in account");
        }

        Operation operation = operationFactory.createExpense(
                accountId, amount, date, description, categoryId);

        operation = operationRepository.save(operation);

        bankAccountRepository.updateBalance(accountId, amount.negate());

        return Optional.of(operation);
    }

    @Transactional
    public Optional<Operation> createOperation(
            OperationType type,
            UUID accountId,
            UUID categoryId,
            BigDecimal amount,
            String description) {

        if (type == OperationType.INCOME) {
            return createIncome(accountId, categoryId, amount, LocalDateTime.now(), description);
        } else {
            return createExpense(accountId, categoryId, amount, LocalDateTime.now(), description);
        }
    }

    public Optional<Operation> getOperationById(UUID id) {
        return operationRepository.findById(id);
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    public List<Operation> getOperationsByType(OperationType type) {
        return operationRepository.findByType(type);
    }

    public List<Operation> getOperationsByDate(LocalDate date) {
        return operationRepository.findByDate(date);
    }

    public List<Operation> getOperationsByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        return operationRepository.findByDateRange(startDateTime, endDateTime);
    }

    public List<Operation> getOperationsByMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return getOperationsByDateRange(startDate, endDate);
    }

    @Transactional
    public boolean deleteOperation(UUID operationId) {
        Optional<Operation> operationOpt = operationRepository.findById(operationId);
        if (operationOpt.isEmpty()) {
            return false;
        }

        Operation operation = operationOpt.get();
        UUID accountId = operation.getBankAccountId();

        BigDecimal adjustmentAmount;
        if (operation.isIncome()) {
            adjustmentAmount = operation.getAmount().negate();
        } else {
            adjustmentAmount = operation.getAmount();
        }

        bankAccountRepository.updateBalance(accountId, adjustmentAmount);

        return operationRepository.deleteById(operationId);
    }

    public BigDecimal getTotalIncome(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        return operationRepository.sumByDateRangeAndType(start, end, OperationType.INCOME);
    }

    public BigDecimal getTotalExpenses(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        return operationRepository.sumByDateRangeAndType(start, end, OperationType.EXPENSE);
    }

    public BigDecimal getBalanceDifference(LocalDate startDate, LocalDate endDate) {
        BigDecimal income = getTotalIncome(startDate, endDate);
        BigDecimal expenses = getTotalExpenses(startDate, endDate);

        if (income == null) {
            income = BigDecimal.ZERO;
        }
        if (expenses == null) {
            expenses = BigDecimal.ZERO;
        }

        return income.subtract(expenses);
    }

    public Map<LocalDate, List<Operation>> getOperationsGroupedByDate(
            LocalDate startDate, LocalDate endDate) {

        List<Operation> operations = getOperationsByDateRange(startDate, endDate);

        Map<LocalDate, List<Operation>> result = new TreeMap<>(); // TreeMap for sorted dates

        for (Operation op : operations) {
            LocalDate date = op.getDate().toLocalDate();
            result.computeIfAbsent(date, k -> new ArrayList<>()).add(op);
        }

        return result;
    }

    public long getOperationCount() {
        return operationRepository.findAll().size();
    }

    @Transactional
    public Optional<Operation> updateOperationDescription(UUID operationId, String newDescription) {
        Optional<Operation> operationOpt = operationRepository.findById(operationId);
        if (operationOpt.isEmpty()) {
            return Optional.empty();
        }

        Operation operation = operationOpt.get();
        operation.setDescription(newDescription);
        return Optional.of(operationRepository.save(operation));
    }

    public Optional<OperationDetails> getOperationDetails(UUID operationId) {
        Optional<Operation> operationOpt = operationRepository.findById(operationId);
        if (operationOpt.isEmpty()) {
            return Optional.empty();
        }

        Operation operation = operationOpt.get();
        String accountName = bankAccountRepository.findById(operation.getBankAccountId())
                .map(BankAccount::getName)
                .orElse("Unknown Account");

        String categoryName = categoryRepository.findById(operation.getCategoryId())
                .map(Category::getName)
                .orElse("Unknown Category");

        return Optional.of(new OperationDetails(operation, accountName, categoryName));
    }

    public List<Operation> getAccountOperations(UUID accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        if (!bankAccountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }

        return operationRepository.findByBankAccountId(accountId);
    }

    public List<Operation> getAccountOperations(UUID accountId, LocalDate startDate, LocalDate endDate) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (!bankAccountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        return operationRepository.findByDateRange(startDateTime, endDateTime)
                .stream()
                .filter(op -> op.getBankAccountId().equals(accountId))
                .toList();
    }

    public static class OperationDetails {
        private final Operation operation;
        private final String accountName;
        private final String categoryName;

        public OperationDetails(Operation operation, String accountName, String categoryName) {
            this.operation = operation;
            this.accountName = accountName;
            this.categoryName = categoryName;
        }

        public Operation getOperation() {
            return operation;
        }

        public String getAccountName() {
            return accountName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        @Override
        public String toString() {
            return String.format("%s: $%.2f on %s\nAccount: %s\nCategory: %s\n%s",
                    operation.getType(),
                    operation.getAmount(),
                    operation.getDate().toLocalDate(),
                    accountName,
                    categoryName,
                    operation.getDescription() != null ? "Description: " + operation.getDescription() : "");
        }
    }
}

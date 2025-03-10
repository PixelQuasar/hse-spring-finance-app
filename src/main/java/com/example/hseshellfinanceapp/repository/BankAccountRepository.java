package com.example.hseshellfinanceapp.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.BankAccount;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hseshellfinanceapp.jooq.Tables.BANK_ACCOUNT;
import static com.example.hseshellfinanceapp.jooq.Tables.OPERATION;

@Repository
public class BankAccountRepository {

    private final DSLContext dsl;

    @Autowired
    public BankAccountRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<BankAccount> findById(UUID id) {
        Record record = dsl.select()
                .from(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.ID.eq(id))
                .fetchOne();

        return Optional.ofNullable(record).map(this::mapToBankAccount);
    }

    public Optional<BankAccount> findByName(String name) {
        Record record = dsl.select()
                .from(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.NAME.eq(name))
                .fetchOne();

        return Optional.ofNullable(record).map(this::mapToBankAccount);
    }

    public List<BankAccount> findAll() {
        return dsl.select()
                .from(BANK_ACCOUNT)
                .fetch()
                .map(this::mapToBankAccount);
    }

    @Transactional
    public BankAccount save(BankAccount bankAccount) {
        boolean exists = dsl.fetchExists(
                dsl.selectFrom(BANK_ACCOUNT)
                        .where(BANK_ACCOUNT.ID.eq(bankAccount.getId()))
        );

        if (exists) {
            dsl.update(BANK_ACCOUNT)
                    .set(BANK_ACCOUNT.NAME, bankAccount.getName())
                    .set(BANK_ACCOUNT.BALANCE, bankAccount.getBalance())
                    .where(BANK_ACCOUNT.ID.eq(bankAccount.getId()))
                    .execute();
        } else {
            dsl.insertInto(BANK_ACCOUNT)
                    .set(BANK_ACCOUNT.ID, bankAccount.getId())
                    .set(BANK_ACCOUNT.NAME, bankAccount.getName())
                    .set(BANK_ACCOUNT.BALANCE, bankAccount.getBalance())
                    .execute();
        }

        return bankAccount;
    }

    @Transactional
    public boolean deleteById(UUID id) {
        int affected = dsl.deleteFrom(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.ID.eq(id))
                .execute();
        return affected > 0;
    }

    @Transactional
    public Optional<BankAccount> updateBalance(UUID id, BigDecimal amount) {
        Record record = dsl.select(BANK_ACCOUNT.BALANCE)
                .from(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.ID.eq(id))
                .fetchOne();

        if (record == null) {
            return Optional.empty();
        }

        BigDecimal currentBalance = record.get(BANK_ACCOUNT.BALANCE);
        BigDecimal newBalance = currentBalance.add(amount);

        dsl.update(BANK_ACCOUNT)
                .set(BANK_ACCOUNT.BALANCE, newBalance)
                .where(BANK_ACCOUNT.ID.eq(id))
                .execute();

        return findById(id);
    }

    @Transactional
    public Optional<BankAccount> recalculateBalance(UUID id) {
        if (!dsl.fetchExists(dsl.selectFrom(BANK_ACCOUNT).where(BANK_ACCOUNT.ID.eq(id)))) {
            return Optional.empty();
        }

        BigDecimal totalIncome = dsl.select(OPERATION.AMOUNT.sum())
                .from(OPERATION)
                .where(OPERATION.BANK_ACCOUNT_ID.eq(id))
                .and(OPERATION.TYPE.eq(OperationType.INCOME.name()))
                .fetchOne(0, BigDecimal.class);

        if (totalIncome == null) {
            totalIncome = BigDecimal.ZERO;
        }

        BigDecimal totalExpenses = dsl.select(OPERATION.AMOUNT.sum())
                .from(OPERATION)
                .where(OPERATION.BANK_ACCOUNT_ID.eq(id))
                .and(OPERATION.TYPE.eq(OperationType.EXPENSE.name()))
                .fetchOne(0, BigDecimal.class);

        if (totalExpenses == null) {
            totalExpenses = BigDecimal.ZERO;
        }

        BigDecimal calculatedBalance = totalIncome.subtract(totalExpenses);

        dsl.update(BANK_ACCOUNT)
                .set(BANK_ACCOUNT.BALANCE, calculatedBalance)
                .where(BANK_ACCOUNT.ID.eq(id))
                .execute();

        return findById(id);
    }

    private BankAccount mapToBankAccount(Record record) {
        return new BankAccount(
                record.get(BANK_ACCOUNT.ID),
                record.get(BANK_ACCOUNT.NAME),
                record.get(BANK_ACCOUNT.BALANCE)
        );
    }

    public BigDecimal getTotalBalance() {
        return dsl.select(BANK_ACCOUNT.BALANCE.sum())
                .from(BANK_ACCOUNT)
                .fetchOne(0, BigDecimal.class);
    }

    public boolean existsById(UUID id) {
        return dsl.fetchExists(
                dsl.selectFrom(BANK_ACCOUNT)
                        .where(BANK_ACCOUNT.ID.eq(id))
        );
    }

    public boolean existsByName(String name) {
        return dsl.fetchExists(
                dsl.selectFrom(BANK_ACCOUNT)
                        .where(BANK_ACCOUNT.NAME.eq(name))
        );
    }

    public List<BankAccount> findByBalanceGreaterThan(BigDecimal minBalance) {
        return dsl.select()
                .from(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.BALANCE.greaterThan(minBalance))
                .fetch()
                .map(this::mapToBankAccount);
    }

    public List<BankAccount> findByBalanceLessThan(BigDecimal maxBalance) {
        return dsl.select()
                .from(BANK_ACCOUNT)
                .where(BANK_ACCOUNT.BALANCE.lessThan(maxBalance))
                .fetch()
                .map(this::mapToBankAccount);
    }

    public int count() {
        return dsl.fetchCount(dsl.selectFrom(BANK_ACCOUNT));
    }
}

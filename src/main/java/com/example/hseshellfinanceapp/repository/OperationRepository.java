package com.example.hseshellfinanceapp.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.hseshellfinanceapp.domain.model.Operation;
import com.example.hseshellfinanceapp.domain.model.OperationType;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hseshellfinanceapp.jooq.tables.Operation.OPERATION;

@Repository
public class OperationRepository {

    private final DSLContext dsl;

    @Autowired
    public OperationRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<Operation> findById(UUID id) {
        Record record = dsl.select()
                .from(OPERATION)
                .where(OPERATION.ID.eq(id))
                .fetchOne();

        return Optional.ofNullable(record).map(this::mapToOperation);
    }

    public List<Operation> findAll() {
        return dsl.select()
                .from(OPERATION)
                .fetch()
                .map(this::mapToOperation);
    }

    public List<Operation> findByBankAccountId(UUID bankAccountId) {
        return dsl.select()
                .from(OPERATION)
                .where(OPERATION.BANK_ACCOUNT_ID.eq(bankAccountId))
                .fetch()
                .map(this::mapToOperation);
    }

    public List<Operation> findByType(OperationType type) {
        return dsl.select()
                .from(OPERATION)
                .where(OPERATION.TYPE.eq(type.name()))
                .fetch()
                .map(this::mapToOperation);
    }

    public List<Operation> findByCategoryId(UUID categoryId) {
        return dsl.select()
                .from(OPERATION)
                .where(OPERATION.CATEGORY_ID.eq(categoryId))
                .fetch()
                .map(this::mapToOperation);
    }

    public List<Operation> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return dsl.select()
                .from(OPERATION)
                .where(OPERATION.DATE.between(startDate, endDate))
                .orderBy(OPERATION.DATE.desc())
                .fetch()
                .map(this::mapToOperation);
    }

    public List<Operation> findByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);

        return findByDateRange(startOfDay, endOfDay);
    }

    public List<Operation> findAllIncomeOperations() {
        return findByType(OperationType.INCOME);
    }

    public List<Operation> findAllExpenseOperations() {
        return findByType(OperationType.EXPENSE);
    }

    @Transactional
    public Operation save(Operation operation) {
        boolean exists = dsl.fetchExists(
                dsl.selectFrom(OPERATION)
                        .where(OPERATION.ID.eq(operation.getId()))
        );

        if (exists) {
            dsl.update(OPERATION)
                    .set(OPERATION.TYPE, operation.getType().name())
                    .set(OPERATION.BANK_ACCOUNT_ID, operation.getBankAccountId())
                    .set(OPERATION.AMOUNT, operation.getAmount())
                    .set(OPERATION.DATE, operation.getDate())
                    .set(OPERATION.DESCRIPTION, operation.getDescription())
                    .set(OPERATION.CATEGORY_ID, operation.getCategoryId())
                    .where(OPERATION.ID.eq(operation.getId()))
                    .execute();
        } else {
            dsl.insertInto(OPERATION)
                    .set(OPERATION.ID, operation.getId())
                    .set(OPERATION.TYPE, operation.getType().name())
                    .set(OPERATION.BANK_ACCOUNT_ID, operation.getBankAccountId())
                    .set(OPERATION.AMOUNT, operation.getAmount())
                    .set(OPERATION.DATE, operation.getDate())
                    .set(OPERATION.DESCRIPTION, operation.getDescription())
                    .set(OPERATION.CATEGORY_ID, operation.getCategoryId())
                    .execute();
        }

        return operation;
    }

    @Transactional
    public boolean deleteById(UUID id) {
        int affected = dsl.deleteFrom(OPERATION)
                .where(OPERATION.ID.eq(id))
                .execute();
        return affected > 0;
    }

    public BigDecimal sumByType(OperationType type) {
        return dsl.select(OPERATION.AMOUNT.sum())
                .from(OPERATION)
                .where(OPERATION.TYPE.eq(type.name()))
                .fetchOne(0, BigDecimal.class);
    }

    public BigDecimal sumByBankAccountIdAndType(UUID bankAccountId, OperationType type) {
        return dsl.select(OPERATION.AMOUNT.sum())
                .from(OPERATION)
                .where(OPERATION.BANK_ACCOUNT_ID.eq(bankAccountId))
                .and(OPERATION.TYPE.eq(type.name()))
                .fetchOne(0, BigDecimal.class);
    }

    public BigDecimal sumByCategoryId(UUID categoryId) {
        return dsl.select(OPERATION.AMOUNT.sum())
                .from(OPERATION)
                .where(OPERATION.CATEGORY_ID.eq(categoryId))
                .fetchOne(0, BigDecimal.class);
    }

    public BigDecimal sumByDateRangeAndType(LocalDateTime startDate, LocalDateTime endDate, OperationType type) {
        return dsl.select(OPERATION.AMOUNT.sum())
                .from(OPERATION)
                .where(OPERATION.DATE.between(startDate, endDate))
                .and(OPERATION.TYPE.eq(type.name()))
                .fetchOne(0, BigDecimal.class);
    }

    @Transactional
    public int deleteByBankAccountId(UUID bankAccountId) {
        return dsl.deleteFrom(OPERATION)
                .where(OPERATION.BANK_ACCOUNT_ID.eq(bankAccountId))
                .execute();
    }

    private Operation mapToOperation(Record record) {
        return new Operation(
                record.get(OPERATION.ID),
                OperationType.valueOf(record.get(OPERATION.TYPE)),
                record.get(OPERATION.BANK_ACCOUNT_ID),
                record.get(OPERATION.AMOUNT),
                record.get(OPERATION.DATE),
                record.get(OPERATION.DESCRIPTION),
                record.get(OPERATION.CATEGORY_ID)
        );
    }
}
